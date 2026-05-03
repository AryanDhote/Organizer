package com.my.organizer.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.my.organizer.R;
import com.my.organizer.adapters.ToDoAdapter;
import com.my.organizer.models.ToDo;
import com.my.organizer.viewmodel.ToDoViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ToDoListActivity extends AppCompatActivity {

    private ToDoViewModel viewModel;
    private ToDoAdapter adapter;
    private ActionMode actionMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        RecyclerView rv = findViewById(R.id.rv_todos);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ToDoAdapter();
        rv.setAdapter(adapter);

        // ✅ CLICK (edit OR select)
        adapter.setOnToDoClickListener(todo -> {
            if (actionMode != null) {
                adapter.toggleSelection(todo);
                updateTitle();
            } else {
                Intent i = new Intent(this, AddEditToDoActivity.class);
                i.putExtra(AddEditToDoActivity.EXTRA_TODO, todo);
                startActivity(i);
            }
        });

        // ✅ LONG PRESS → start selection
        adapter.setOnToDoLongClickListener(toDo -> {
            if (actionMode == null) {
                actionMode = startSupportActionMode(actionModeCallback);
            }
            adapter.toggleSelection(toDo);
            updateTitle();
        });

        // ViewModel
        viewModel = new ViewModelProvider(this).get(ToDoViewModel.class);
        viewModel.getAllToDos().observe(this, todos -> {
            adapter.setToDoList(todos);
            if (adapter.getSelectedCount() == 0 && actionMode != null) {
                actionMode.finish();
            }
        });
    }

    // ---------------- ACTION MODE ----------------

    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, android.view.Menu menu) {
            getMenuInflater().inflate(R.menu.menu_action_mode_delete, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, android.view.Menu menu) {
            updateTitle();
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, android.view.MenuItem item) {

            if (item.getItemId() == R.id.action_delete) {

                List<ToDo> selected = adapter.getSelectedItems();

                if (!selected.isEmpty()) {
                    for (ToDo t : selected) {
                        viewModel.delete(t);
                    }

                    Snackbar.make(findViewById(android.R.id.content),
                            selected.size() + " deleted",
                            Snackbar.LENGTH_LONG).show();

                    mode.finish();
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