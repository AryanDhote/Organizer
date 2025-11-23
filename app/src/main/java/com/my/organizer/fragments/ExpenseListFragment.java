package com.my.organizer.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.my.organizer.R;
import com.my.organizer.adapters.ExpenseAdapter;
import com.my.organizer.models.Expense;
import com.my.organizer.viewmodel.ExpenseViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ExpenseListFragment extends Fragment {

    public interface OnExpenseListActionListener {
        void onAddExpenseFromList();
        void onEditExpenseFromList(Expense expense);
    }

    private OnExpenseListActionListener listener;
    private ExpenseViewModel expenseViewModel;
    private ExpenseAdapter adapter;
    private ActionMode actionMode;

    public ExpenseListFragment() { }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnExpenseListActionListener) {
            listener = (OnExpenseListActionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnExpenseListActionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expense_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = view.findViewById(R.id.rv_expenses_fragment);
        View parent = view;
        View fab = view.findViewById(R.id.fab_add_expense);

        adapter = new ExpenseAdapter();
        adapter.setSelectionEnabled(true);
        adapter.setShowOverflow(true);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        adapter.setOnExpenseClickListener(expense -> {
            if (actionMode != null) {
                adapter.toggleSelection(expense);
                updateActionModeTitle();
            } else {
                if (listener != null) listener.onEditExpenseFromList(expense);
            }
        });

        adapter.setOnExpenseLongClickListener(expense -> {
            if (actionMode == null) {
                actionMode = ((androidx.appcompat.app.AppCompatActivity) requireActivity())
                        .startSupportActionMode(actionModeCallback);
            }
            adapter.toggleSelection(expense);
            updateActionModeTitle();
        });

        adapter.setOnExpenseActionListener(new ExpenseAdapter.OnExpenseActionListener() {
            @Override
            public void onEditExpense(Expense e) {
                if (listener != null) listener.onEditExpenseFromList(e);
            }
            @Override
            public void onDeleteExpense(Expense e) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Delete")
                        .setMessage("Delete this expense?")
                        .setPositiveButton("Delete", (d, which) -> {
                            expenseViewModel.delete(e);
                            Snackbar.make(parent, "Expense deleted", Snackbar.LENGTH_LONG)
                                    .setAction("UNDO", v -> expenseViewModel.insert(e))
                                    .show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        expenseViewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);
        expenseViewModel.getAllExpenses().observe(getViewLifecycleOwner(), expenses -> {
            adapter.setExpenseList(expenses);
            if (adapter.getSelectedCount() == 0 && actionMode != null) {
                actionMode.finish();
            }
        });

        fab.setOnClickListener(v -> {
            if (listener != null) listener.onAddExpenseFromList();
        });

        // Swipe-to-delete with confirmation
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                Expense swiped = adapter.getItemAt(pos);

                new AlertDialog.Builder(requireContext())
                        .setTitle("Delete")
                        .setMessage("Delete this expense?")
                        .setPositiveButton("Delete", (d, which) -> {
                            expenseViewModel.delete(swiped);
                            Snackbar.make(parent, "Expense deleted", Snackbar.LENGTH_LONG)
                                    .setAction("UNDO", v -> expenseViewModel.insert(swiped))
                                    .show();
                        })
                        .setNegativeButton("Cancel", (d, which) -> adapter.notifyItemChanged(pos))
                        .setOnCancelListener(dialog -> adapter.notifyItemChanged(pos))
                        .show();
            }
        };
        new ItemTouchHelper(swipeCallback).attachToRecyclerView(rv);
    }

    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, android.view.Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_action_mode_delete, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, android.view.Menu menu) {
            updateActionModeTitle();
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.action_delete) {
                List<Expense> deleted = new ArrayList<>(adapter.getSelectedItems());
                if (!deleted.isEmpty()) {
                    for (Expense e : deleted) {
                        expenseViewModel.delete(e);
                    }
                    mode.finish();
                    Snackbar.make(requireView(), deleted.size() + " expense(s) deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", v -> {
                                for (Expense e : deleted) {
                                    expenseViewModel.insert(e);
                                }
                            }).show();
                }
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelection();
            actionMode = null;
        }
    };

    private void updateActionModeTitle() {
        if (actionMode == null) return;
        int count = adapter.getSelectedCount();
        actionMode.setTitle(count + " selected");
    }
}
