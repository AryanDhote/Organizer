package com.my.organizer.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.my.organizer.R;
import com.my.organizer.models.ToDo;
import com.my.organizer.viewmodel.ToDoViewModel;

import java.util.Calendar;
import java.util.Date;

public class AddEditToDoActivity extends AppCompatActivity {

    public static final String EXTRA_TODO = "todo";

    private EditText inputTitle, inputDescription;
    private TextView textDueDate;
    private Button btnSave;
    private ToDoViewModel toDoViewModel;
    private ToDo editingToDo;
    private Date selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_todo);

        inputTitle = findViewById(R.id.input_todo_title);
        inputDescription = findViewById(R.id.input_todo_description);
        textDueDate = findViewById(R.id.text_todo_date);
        btnSave = findViewById(R.id.btn_save_todo);

        toDoViewModel = new ViewModelProvider(this).get(ToDoViewModel.class);

        // Check if editing
        Intent i = getIntent();
        if (i != null && i.hasExtra(EXTRA_TODO)) {
            editingToDo = (ToDo) i.getSerializableExtra(EXTRA_TODO);
        }

        if (editingToDo != null) {
            setTitle("Edit ToDo");
            inputTitle.setText(editingToDo.getTitle());
            inputDescription.setText(editingToDo.getDescription());
            selectedDate = editingToDo.getDueDate();
            if (selectedDate != null) {
                textDueDate.setText(android.text.format.DateFormat.format("dd MMM yyyy", selectedDate));
            }
        } else {
            setTitle("Add ToDo");
            selectedDate = new Date();
            textDueDate.setText(android.text.format.DateFormat.format("dd MMM yyyy", selectedDate));
        }

        textDueDate.setOnClickListener(v -> showDatePicker());

        btnSave.setOnClickListener(v -> {
            String title = inputTitle.getText().toString().trim();
            String desc = inputDescription.getText().toString().trim();
            if (title.isEmpty()) {
                inputTitle.setError("Title required");
                return;
            }

            if (editingToDo == null) {
                ToDo t = new ToDo(title, desc, selectedDate, "");
                toDoViewModel.insert(t);
                Toast.makeText(this, "ToDo added", Toast.LENGTH_SHORT).show();
            } else {
                editingToDo.setTitle(title);
                editingToDo.setDescription(desc);
                editingToDo.setDueDate(selectedDate);
                toDoViewModel.update(editingToDo);
                Toast.makeText(this, "ToDo updated", Toast.LENGTH_SHORT).show();
            }
            finish();
        });
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        c.setTime(selectedDate != null ? selectedDate : new Date());
        new DatePickerDialog(this, (DatePicker view, int year, int month, int day) -> {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);
            selectedDate = cal.getTime();
            textDueDate.setText(android.text.format.DateFormat.format("dd MMM yyyy", selectedDate));
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }
}
