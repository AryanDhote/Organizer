package com.my.organizer.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.my.organizer.R;
import com.my.organizer.models.Expense;
import com.my.organizer.models.ExpenseType;
import com.my.organizer.viewmodel.ExpenseViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditExpenseActivity extends AppCompatActivity {

    public static final String EXTRA_EXPENSE = "expense";

    private TextInputEditText inputAmount, inputNote;
    private Spinner spinnerType;
    private TextView textDate;
    private MaterialButton btnSave;
    private MaterialButton btnDelete;
    private ExpenseViewModel expenseViewModel;
    private Date selectedDate;
    private Expense editingExpense;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_expense);

        inputAmount = findViewById(R.id.input_amount);
        inputNote = findViewById(R.id.input_note);
        spinnerType = findViewById(R.id.spinner_type);
        textDate = findViewById(R.id.text_date);
        btnSave = findViewById(R.id.btn_save);
        btnDelete = findViewById(R.id.btn_delete);

        // Defensive tinting so the delete button looks identical in light & dark modes
        applyDeleteButtonTint();

        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);

        // spinner with enum values
        spinnerType.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                ExpenseType.values()
        ));

        Intent i = getIntent();
        if (i != null && i.hasExtra(EXTRA_EXPENSE)) {
            editingExpense = (Expense) i.getSerializableExtra(EXTRA_EXPENSE);
        }

        if (editingExpense != null) {
            setTitle("Edit Expense");
            inputAmount.setText(String.valueOf(editingExpense.getAmount()));
            inputNote.setText(editingExpense.getNote());
            selectedDate = editingExpense.getDate();
            textDate.setText(selectedDate == null ? "" : sdf.format(selectedDate));
            spinnerType.setSelection(editingExpense.getType() == null ? 0 : editingExpense.getType().ordinal());
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            setTitle("Add Expense");
            selectedDate = new Date();
            textDate.setText(sdf.format(selectedDate));
            btnDelete.setVisibility(View.GONE);
        }

        textDate.setOnClickListener(v -> showDatePicker());

        btnSave.setOnClickListener(v -> {
            String amountStr = inputAmount.getText() == null ? "" : inputAmount.getText().toString().trim();
            String note = inputNote.getText() == null ? "" : inputNote.getText().toString().trim();
            if (amountStr.isEmpty()) {
                inputAmount.setError("Amount required");
                return;
            }
            double amount;
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException ex) {
                inputAmount.setError("Invalid amount");
                return;
            }
            ExpenseType type = (ExpenseType) spinnerType.getSelectedItem();

            if (editingExpense == null) {
                Expense expense = new Expense(amount, selectedDate, type, note);
                expenseViewModel.insert(expense);
                Toast.makeText(this, "Expense added", Toast.LENGTH_SHORT).show();
            } else {
                editingExpense.setAmount(amount);
                editingExpense.setDate(selectedDate);
                editingExpense.setType(type);
                editingExpense.setNote(note);
                expenseViewModel.update(editingExpense);
                Toast.makeText(this, "Expense updated", Toast.LENGTH_SHORT).show();
            }
            finish();
        });

        btnDelete.setOnClickListener(v -> {
            if (editingExpense == null) return;

            new AlertDialog.Builder(this)
                    .setTitle("Delete")
                    .setMessage("Delete this expense?")
                    .setPositiveButton("Delete", (dialog, which) -> performDeleteWithUndo(editingExpense))
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    private void applyDeleteButtonTint() {
        if (btnDelete == null) return;
        int redColor = ContextCompat.getColor(this, R.color.red);
        btnDelete.setBackgroundTintList(ColorStateList.valueOf(redColor));
        btnDelete.setIconTint(ColorStateList.valueOf(
                ContextCompat.getColor(this, android.R.color.white)
        ));
        // ensure no stroke
        btnDelete.setStrokeWidth(0);
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        c.setTime(selectedDate != null ? selectedDate : new Date());
        new DatePickerDialog(this, (DatePicker view, int year, int month, int day) -> {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);
            selectedDate = cal.getTime();
            textDate.setText(sdf.format(selectedDate));
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * Deletes the item and shows a Snackbar UNDO. Disables delete button to avoid double-tap.
     * Finishes the activity so user returns to list while Snackbar remains visible there.
     */
    private void performDeleteWithUndo(Expense toDelete) {
        if (toDelete == null) return;

        // prevent repeated taps
        btnDelete.setEnabled(false);

        // delete from DB (repository handles background thread)
        expenseViewModel.delete(toDelete);

        // show snackbar with UNDO action; use root view so it attaches to coordinator if present
        View root = findViewById(android.R.id.content);
        Snackbar.make(root, "Expense deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO", v -> {
                    // re-insert the same object
                    expenseViewModel.insert(toDelete);
                })
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        btnDelete.setEnabled(true);
                    }
                })
                .show();

        // finish activity (UNDO still available from list screen)
        finish();
    }
}
