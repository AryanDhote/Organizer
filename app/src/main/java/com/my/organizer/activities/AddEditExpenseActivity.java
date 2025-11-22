package com.my.organizer.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
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

    private EditText inputAmount, inputNote;
    private Spinner spinnerType;
    private TextView textDate;
    private MaterialButton btnSave;
    private ExpenseViewModel expenseViewModel;
    private Date selectedDate;
    private Expense editingExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_expense);

        inputAmount = findViewById(R.id.input_amount);
        inputNote = findViewById(R.id.input_note);
        spinnerType = findViewById(R.id.spinner_type);
        textDate = findViewById(R.id.text_date);
        btnSave = findViewById(R.id.btn_save);

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
            textDate.setText(selectedDate == null ? "" :
                    new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(selectedDate));
            spinnerType.setSelection(editingExpense.getType() == null ? 0 : editingExpense.getType().ordinal());
        } else {
            setTitle("Add Expense");
            selectedDate = new Date();
            textDate.setText(new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(selectedDate));
        }

        textDate.setOnClickListener(v -> showDatePicker());

        btnSave.setOnClickListener(v -> {
            String amountStr = inputAmount.getText().toString().trim();
            String note = inputNote.getText().toString().trim();
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
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        c.setTime(selectedDate != null ? selectedDate : new Date());
        new DatePickerDialog(this, (DatePicker view, int year, int month, int day) -> {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);
            selectedDate = cal.getTime();
            textDate.setText(new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(selectedDate));
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }
}
