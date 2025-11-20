// ToDoListActivity.java
package com.my.organizer.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.my.organizer.R;
import com.my.organizer.adapters.ToDoAdapter;
import com.my.organizer.models.ToDo;
import com.my.organizer.viewmodel.ToDoViewModel;

import java.util.List;

public class ToDoListActivity extends AppCompatActivity {

    private ToDoViewModel toDoViewModel;
    private ToDoAdapter toDoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        RecyclerView recyclerView = findViewById(R.id.recycler_todos);
        MaterialButton addBtn = findViewById(R.id.btn_add_todo);

        toDoAdapter = new ToDoAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(toDoAdapter);

        toDoViewModel = new ViewModelProvider(this).get(ToDoViewModel.class);
        toDoViewModel.getAllToDos().observe(this, new Observer<List<ToDo>>() {
            @Override
            public void onChanged(List<ToDo> toDos) {
                toDoAdapter.setToDoList(toDos);
            }
        });

        addBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditToDoActivity.class);
            startActivity(intent);
        });
    }
}
