package com.my.organizer.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

        RecyclerView recyclerView = findViewById(R.id.recyclerViewToDos);
        FloatingActionButton fabAddToDo = findViewById(R.id.fabAddToDo);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new ToDoAdapter(this); // ✅ Pass context to adapter
        recyclerView.setAdapter(adapter);

        toDoViewModel = new ViewModelProvider(this).get(ToDoViewModel.class);
        toDoViewModel.getAllToDos().observe(this, todos -> adapter.submitList(todos));

        fabAddToDo.setOnClickListener(v -> {
            Intent intent = new Intent(ToDoListActivity.this, AddEditToDoActivity.class);
            startActivity(intent);
        });
    }
}
