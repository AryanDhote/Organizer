package com.my.organizer.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.my.organizer.R;
import com.my.organizer.models.ToDo;
import com.my.organizer.viewmodel.ToDoViewModel;

import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class AddEditToDoActivity extends AppCompatActivity {

    public static final String EXTRA_TODO = "todo";

    private TextInputEditText inputTitle, inputDescription;
    private TextView textDueDate, textExpiryTime;
    private MaterialButton btnSave;
    private MaterialButton btnDelete;

    private ToDoViewModel toDoViewModel;
    private ToDo editingToDo;
    private Date selectedDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_todo);

        inputTitle = findViewById(R.id.input_todo_title);
        inputDescription = findViewById(R.id.input_todo_description);
        textDueDate = findViewById(R.id.text_todo_date);
        textExpiryTime = findViewById(R.id.text_todo_expiry_time);
        btnSave = findViewById(R.id.btn_save_todo);
        btnDelete = findViewById(R.id.btn_delete);

        toDoViewModel = new ViewModelProvider(this).get(ToDoViewModel.class);

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
            } else {
                textDueDate.setText("Tap to select due date");
            }
            textExpiryTime.setText(editingToDo.getExpiryTime() == null ? "" : editingToDo.getExpiryTime());
            // show delete button in edit mode
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            setTitle("Add ToDo");
            selectedDate = new Date();
            textDueDate.setText(android.text.format.DateFormat.format("dd MMM yyyy", selectedDate));
            btnDelete.setVisibility(View.GONE);
        }

        textDueDate.setOnClickListener(v -> showDatePicker());

        textExpiryTime.setOnClickListener(v -> {
            // optional: implement time picker later
        });

        btnSave.setOnClickListener(v -> {
            String title = inputTitle.getText() == null ? "" : inputTitle.getText().toString().trim();
            String desc = inputDescription.getText() == null ? "" : inputDescription.getText().toString().trim();
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

        btnDelete.setOnClickListener(v -> {
            if (editingToDo == null) return;

            new AlertDialog.Builder(this)
                    .setTitle("Delete")
                    .setMessage("Delete this ToDo?")
                    .setPositiveButton("Delete", (dialog, which) -> performDeleteWithUndo(editingToDo))
                    .setNegativeButton("Cancel", null)
                    .show();
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

    /**
     * Delete with UNDO: disables delete button, deletes via ViewModel, shows Snackbar with UNDO that reinserts.
     * Finishes the activity so user returns to the list while Snackbar remains visible there.
     */
    private void performDeleteWithUndo(ToDo toDelete) {
        if (toDelete == null) return;

        // prevent double taps
        btnDelete.setEnabled(false);

        // delete
        toDoViewModel.delete(toDelete);

        // show Snackbar with UNDO
        View root = findViewById(android.R.id.content);
        Snackbar.make(root, "ToDo deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO", v -> {
                    toDoViewModel.insert(toDelete);
                })
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        // defensive: re-enable delete if user returns here (we normally finish)
                        btnDelete.setEnabled(true);
                    }
                })
                .show();

        // finish activity immediately (UNDO still works)
        finish();
    }
}
