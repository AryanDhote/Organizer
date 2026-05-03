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
    private ToDoAdapter adapter;
    private ToDoViewModel viewModel;
    private ActionMode actionMode;
    private ToDoViewModel toDoViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnToDoListActionListener) {
            listener = (OnToDoListActionListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_todo_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        toDoViewModel = new ViewModelProvider(requireActivity())
                .get(ToDoViewModel.class);

        RecyclerView rv = view.findViewById(R.id.rv_todos_fragment);
        View fab = view.findViewById(R.id.fab_add_todo);

        adapter = new ToDoAdapter();
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        viewModel = new ViewModelProvider(requireActivity())
                .get(ToDoViewModel.class);

        // ---------------- CLICK ----------------
        adapter.setOnToDoClickListener(todo -> {
            if (actionMode != null) {
                adapter.toggleSelection(todo);
                updateTitle();
            } else {
                if (listener != null) listener.onEditToDoFromList(todo);
            }
        });

        // ---------------- LONG CLICK ----------------
        adapter.setOnToDoClickListener(toDo -> {

            if (actionMode != null) {
                adapter.toggleSelection(toDo);
                updateTitle();
                adapter.notifyDataSetChanged(); // ✅
            } else {
                if (listener != null) listener.onEditToDoFromList(toDo);
            }
        });

        adapter.setOnToDoLongClickListener(toDo -> {

            if (actionMode == null) {
                actionMode = ((AppCompatActivity) requireActivity())
                        .startSupportActionMode(actionModeCallback);
            }

            adapter.toggleSelection(toDo);
            updateTitle();
            adapter.notifyDataSetChanged(); // ✅
        });

        // ---------------- DATA ----------------
        viewModel.getAllToDos().observe(getViewLifecycleOwner(), todos -> {
            adapter.setToDoList(todos);

            if (adapter.getSelectedCount() == 0 && actionMode != null) {
                actionMode.finish();
            }
        });

        // ---------------- FAB ----------------
        fab.setOnClickListener(v -> {
            if (listener != null) listener.onAddToDoFromList();
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

                List<ToDo> selected = new ArrayList<>(adapter.getSelectedItems());

                if (!selected.isEmpty()) {

                    // ✅ MULTI DELETE FIX
                    toDoViewModel.deleteBulk(selected);

                    adapter.clearSelection();
                    mode.finish();

                    Snackbar.make(requireView(),
                                    selected.size() + " item(s) deleted",
                                    Snackbar.LENGTH_LONG)
                            .setAction("UNDO", v -> {
                                for (ToDo t : selected) {
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

    private void updateTitle() {
        if (actionMode != null) {
            actionMode.setTitle(adapter.getSelectedCount() + " selected");
        }
    }
}