package com.my.organizer.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.my.organizer.R;
import com.my.organizer.adapters.ToDoAdapter;
import com.my.organizer.models.ToDo;
import com.my.organizer.viewmodel.ToDoViewModel;
import com.google.android.material.snackbar.Snackbar;

public class ToDoListActivity extends AppCompatActivity {

    private ToDoViewModel toDoViewModel;
    private ToDoAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        RecyclerView rv = findViewById(R.id.rv_todos);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ToDoAdapter(this); // adapter has an overload that accepts Context
        rv.setAdapter(adapter);

        // single-tap -> edit
        adapter.setOnToDoClickListener(new ToDoAdapter.ToDoClickListener() {
            @Override
            public void onToDoClick(ToDo toDo) {
                Intent i = new Intent(ToDoListActivity.this, AddEditToDoActivity.class);
                i.putExtra(AddEditToDoActivity.EXTRA_TODO, toDo);
                startActivity(i);
            }
        });

        // optional: overflow actions (Edit / Delete) from item popup menu
        adapter.setOnToDoActionListener(new ToDoAdapter.ToDoActionListener() {
            @Override
            public void onEditToDo(ToDo toDo) {
                Intent i = new Intent(ToDoListActivity.this, AddEditToDoActivity.class);
                i.putExtra(AddEditToDoActivity.EXTRA_TODO, toDo);
                startActivity(i);
            }

            @Override
            public void onDeleteToDo(ToDo toDo) {
                // confirm and delete with UNDO
                new AlertDialog.Builder(ToDoListActivity.this)
                        .setTitle("Delete")
                        .setMessage("Delete this item?")
                        .setPositiveButton("Delete", (d, which) -> {
                            toDoViewModel.delete(toDo);
                            Snackbar.make(rv, "Deleted", Snackbar.LENGTH_LONG)
                                    .setAction("UNDO", v -> toDoViewModel.insert(toDo))
                                    .show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        toDoViewModel = new ViewModelProvider(this).get(ToDoViewModel.class);
        toDoViewModel.getAllToDos().observe(this, todos -> adapter.setToDoList(todos));
    }
}
