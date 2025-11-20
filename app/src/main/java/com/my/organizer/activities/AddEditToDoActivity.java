package com.my.organizer.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.my.organizer.R;
import com.my.organizer.models.ToDo;
import com.my.organizer.viewmodel.ToDoViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// Import Parcelable if needed
public class AddEditToDoActivity extends AppCompatActivity {

    private EditText titleInput, descriptionInput;
    private TextView dateText, timeText;
    private Button saveButton, deleteButton;

    private Date selectedDate = new Date();
    private String selectedTime = "";
    private ToDoViewModel toDoViewModel;

    private ToDo existingToDo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_todo);

        titleInput = findViewById(R.id.input_title);
        descriptionInput = findViewById(R.id.input_description);
        dateText = findViewById(R.id.text_due_date);
        timeText = findViewById(R.id.text_expiry_time);
        saveButton = findViewById(R.id.btn_save_todo);
        deleteButton = findViewById(R.id.btn_delete_todo);

        toDoViewModel = new ViewModelProvider(this).get(ToDoViewModel.class);

        // Check if editing an existing ToDo
        if (getIntent().hasExtra("todo")) {
            existingToDo = (ToDo) getIntent().getSerializableExtra("todo");
            titleInput.setText(existingToDo.getTitle());
            descriptionInput.setText(existingToDo.getDescription());
            selectedDate = existingToDo.getDueDate();
            selectedTime = existingToDo.getExpiryTime();
            updateDateDisplay();
            timeText.setText(selectedTime);
            deleteButton.setVisibility(View.VISIBLE); // Show delete button
        } else {
            updateDateDisplay();
            timeText.setText("Set Time");
        }

        dateText.setOnClickListener(v -> showDatePicker());
        timeText.setOnClickListener(v -> showTimePicker());

        saveButton.setOnClickListener(v -> saveToDo());
        deleteButton.setOnClickListener(v -> deleteToDo());
    }

    private void updateDateDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        dateText.setText(sdf.format(selectedDate));
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);

        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            selectedDate = calendar.getTime();
            updateDateDisplay();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
            timeText.setText(selectedTime);
        }, hour, minute, true).show();
    }

    private void saveToDo() {
        String title = titleInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();

        if (title.isEmpty()) {
            titleInput.setError("Title required");
            return;
        }

        if (existingToDo != null) {
            existingToDo.setTitle(title);
            existingToDo.setDescription(description);
            existingToDo.setDueDate(selectedDate);
            existingToDo.setExpiryTime(selectedTime);
            toDoViewModel.update(existingToDo);
        } else {
            ToDo toDo = new ToDo(title, description, selectedDate, selectedTime);
            toDoViewModel.insert(toDo);
        }

        finish();
    }

    private void deleteToDo() {
        if (existingToDo != null) {
            toDoViewModel.delete(existingToDo);
            finish();
        }
    }
}
