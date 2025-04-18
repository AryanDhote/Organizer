package com.my.organizer.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.my.organizer.R;
import com.my.organizer.adapters.ToDoAdapter;
import com.my.organizer.viewmodel.ToDoViewModel;

public class ToDoListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddToDo;
    private ToDoViewModel toDoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        // Initialize UI elements
        recyclerView = findViewById(R.id.recyclerViewToDos);
        fabAddToDo = findViewById(R.id.fabAddToDo);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ToDoAdapter adapter = new ToDoAdapter();
        recyclerView.setAdapter(adapter);

        // ViewModel setup
        toDoViewModel = new ViewModelProvider(this).get(ToDoViewModel.class);
        toDoViewModel.getAllToDos().observe(this, adapter::submitList);

        // Add new To-Do
        fabAddToDo.setOnClickListener(v -> {
            Intent intent = new Intent(ToDoListActivity.this, AddEditToDoActivity.class);
            startActivity(intent);
        });
    }
}
