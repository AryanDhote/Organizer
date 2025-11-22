package com.my.organizer.activities;

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

public class ToDoListActivity extends AppCompatActivity {

    private ToDoViewModel toDoViewModel;
    private ToDoAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        RecyclerView rv = findViewById(R.id.rv_todos);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ToDoAdapter(this);
        rv.setAdapter(adapter);

        // enable click-to-edit
        adapter.setOnToDoClickListener(new ToDoAdapter.OnToDoClickListener() {
            @Override
            public void onToDoClick(ToDo toDo) {
                Intent i = new Intent(ToDoListActivity.this, AddEditToDoActivity.class);
                i.putExtra(AddEditToDoActivity.EXTRA_TODO, toDo);
                startActivity(i);
            }
        });

        toDoViewModel = new ViewModelProvider(this).get(ToDoViewModel.class);
        toDoViewModel.getAllToDos().observe(this, todos -> adapter.setToDoList(todos));
    }
}
