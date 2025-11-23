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
import com.my.organizer.adapters.ToDoAdapter;
import com.my.organizer.models.ToDo;
import com.my.organizer.viewmodel.ToDoViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ToDoListFragment extends Fragment {

    public interface OnToDoListActionListener {
        void onAddToDoFromList();
        void onEditToDoFromList(ToDo toDo);
    }

    private OnToDoListActionListener listener;
    private ToDoViewModel toDoViewModel;
    private ToDoAdapter adapter;
    private ActionMode actionMode;

    public ToDoListFragment() { }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnToDoListActionListener) {
            listener = (OnToDoListActionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnToDoListActionListener");
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
        return inflater.inflate(R.layout.fragment_todo_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = view.findViewById(R.id.rv_todos_fragment);
        View parent = view;
        View fab = view.findViewById(R.id.fab_add_todo);

        adapter = new ToDoAdapter();
        // Enable selection mode and overflow for full list
        adapter.setSelectionEnabled(true);
        adapter.setShowOverflow(true);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        // click -> edit (single tap)
        adapter.setOnToDoClickListener(toDo -> {
            if (actionMode != null) {
                adapter.toggleSelection(toDo);
                updateActionModeTitle();
            } else {
                if (listener != null) listener.onEditToDoFromList(toDo);
            }
        });

        // long click -> start/modify selection
        adapter.setOnToDoLongClickListener(toDo -> {
            if (actionMode == null) {
                actionMode = ((androidx.appcompat.app.AppCompatActivity) requireActivity())
                        .startSupportActionMode(actionModeCallback);
            }
            adapter.toggleSelection(toDo);
            updateActionModeTitle();
        });

        // overflow actions
        adapter.setOnToDoActionListener(new ToDoAdapter.ToDoActionListener() {
            @Override
            public void onEditToDo(ToDo t) {
                if (listener != null) listener.onEditToDoFromList(t);
            }
            @Override
            public void onDeleteToDo(ToDo t) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Delete")
                        .setMessage("Delete this item?")
                        .setPositiveButton("Delete", (d, which) -> {
                            toDoViewModel.delete(t);
                            Snackbar.make(parent, "Deleted", Snackbar.LENGTH_LONG)
                                    .setAction("UNDO", v -> toDoViewModel.insert(t))
                                    .show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        toDoViewModel = new ViewModelProvider(requireActivity()).get(ToDoViewModel.class);
        toDoViewModel.getAllToDos().observe(getViewLifecycleOwner(), todos -> {
            adapter.setToDoList(todos);
            if (adapter.getSelectedCount() == 0 && actionMode != null) {
                actionMode.finish();
            }
        });

        fab.setOnClickListener(v -> {
            if (listener != null) listener.onAddToDoFromList();
        });

        // swipe-to-delete (same as before)
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
                ToDo swiped = adapter.getItemAt(pos);

                new AlertDialog.Builder(requireContext())
                        .setTitle("Delete")
                        .setMessage("Delete this item?")
                        .setPositiveButton("Delete", (d, which) -> {
                            toDoViewModel.delete(swiped);
                            Snackbar.make(parent, "Deleted", Snackbar.LENGTH_LONG)
                                    .setAction("UNDO", v -> toDoViewModel.insert(swiped))
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
                List<ToDo> deleted = new ArrayList<>(adapter.getSelectedItems());
                if (!deleted.isEmpty()) {
                    for (ToDo t : deleted) {
                        toDoViewModel.delete(t);
                    }
                    mode.finish();
                    Snackbar.make(requireView(), deleted.size() + " item(s) deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", v -> {
                                for (ToDo t : deleted) {
                                    toDoViewModel.insert(t);
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
