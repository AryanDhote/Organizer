package com.my.organizer.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.my.organizer.R;
import com.my.organizer.models.ToDo;
import com.my.organizer.viewmodel.ToDoViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditToDoActivity extends AppCompatActivity {

    private EditText edtTitle, edtDescription, edtDate;
    private Button btnSave;
    private ToDoViewModel toDoViewModel;
    private Date selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_todo);

        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        edtDate = findViewById(R.id.edtDate);
        btnSave = findViewById(R.id.btnSave);

        toDoViewModel = new ViewModelProvider(this).get(ToDoViewModel.class);

        edtDate.setOnClickListener(view -> showDatePickerDialog());

        btnSave.setOnClickListener(view -> saveToDo());
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    selectedDate = calendar.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    edtDate.setText(sdf.format(selectedDate));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void saveToDo() {
        String title = edtTitle.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || selectedDate == null) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        ToDo toDo = new ToDo(title, description, selectedDate);
        toDoViewModel.insert(toDo);
        Toast.makeText(this, "ToDo saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}
