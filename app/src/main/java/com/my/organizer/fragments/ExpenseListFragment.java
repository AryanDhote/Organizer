package com.my.organizer.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private ExpenseAdapter adapter;
    private ExpenseViewModel viewModel;
    private ActionMode actionMode;
    private ExpenseViewModel expenseViewModel;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnExpenseListActionListener) {
            listener = (OnExpenseListActionListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expense_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        expenseViewModel = new ViewModelProvider(requireActivity())
                .get(ExpenseViewModel.class);

        RecyclerView rv = view.findViewById(R.id.rv_expenses_fragment);
        View fab = view.findViewById(R.id.fab_add_expense);

        adapter = new ExpenseAdapter();
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity())
                .get(ExpenseViewModel.class);

        // ---------------- CLICK ----------------
        adapter.setOnExpenseClickListener(expense -> {

            if (actionMode != null) {
                adapter.toggleSelection(expense);
                updateTitle();
                adapter.notifyDataSetChanged(); // ✅ important
            } else {
                if (listener != null) listener.onEditExpenseFromList(expense);
            }
        });

        // ---------------- LONG CLICK ----------------
        adapter.setOnExpenseLongClickListener(expense -> {

            if (actionMode == null) {
                actionMode = ((AppCompatActivity) requireActivity())
                        .startSupportActionMode(actionModeCallback);
            }

            adapter.toggleSelection(expense);
            updateTitle();
            adapter.notifyDataSetChanged(); // ✅ important
        });

        // ---------------- DATA ----------------
        viewModel.getAllExpenses().observe(getViewLifecycleOwner(), expenses -> {
            adapter.setExpenseList(expenses);

            if (adapter.getSelectedCount() == 0 && actionMode != null) {
                actionMode.finish();
            }
        });

        // ---------------- FAB ----------------
        fab.setOnClickListener(v -> {
            if (listener != null) listener.onAddExpenseFromList();
        });
    }

    // ---------------- ACTION MODE ----------------

    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode_delete, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            updateTitle();
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            if (item.getItemId() == R.id.action_delete) {

                List<Expense> selected = new ArrayList<>(adapter.getSelectedItems());

                if (!selected.isEmpty()) {

                    // ✅ MULTI DELETE FIX
                    expenseViewModel.deleteBulk(selected);

                    adapter.clearSelection();
                    mode.finish();

                    Snackbar.make(requireView(),
                                    selected.size() + " expense(s) deleted",
                                    Snackbar.LENGTH_LONG)
                            .setAction("UNDO", v -> {
                                for (Expense e : selected) {
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

    private void updateTitle() {
        if (actionMode != null) {
            actionMode.setTitle(adapter.getSelectedCount() + " selected");
        }
    }
}