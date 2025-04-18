package com.my.organizer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.my.organizer.R;
import com.my.organizer.models.ToDo;
import com.my.organizer.viewmodel.ToDoViewModel;

public class AddEditToDoActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription;
    private Button buttonSave;
    private ToDoViewModel toDoViewModel;
    private int toDoId = -1; // Default to -1, meaning it's a new To-Do

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_todo);

        editTextTitle = findViewById(R.id.todoTitleEditText);
        editTextDescription = findViewById(R.id.todoDescriptionEditText);
        buttonSave = findViewById(R.id.saveToDoButton);

        toDoViewModel = new ViewModelProvider(this).get(ToDoViewModel.class);

        Intent intent = getIntent();
        if (intent.hasExtra("todo_id")) {
            setTitle("Edit To-Do");
            toDoId = intent.getIntExtra("todo_id", -1);
            editTextTitle.setText(intent.getStringExtra("todo_title"));
            editTextDescription.setText(intent.getStringExtra("todo_description"));
        } else {
            setTitle("Add To-Do");
        }

        buttonSave.setOnClickListener(v -> saveToDo());
    }

    private void saveToDo() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        ToDo toDo = new ToDo(title, description);

        if (toDoId != -1) {
            toDo.setId(toDoId);
            toDoViewModel.updateToDo(toDo);
        } else {
            toDoViewModel.insertToDo(toDo);
        }

        Toast.makeText(this, "To-Do saved", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
}
