package com.my.organizer.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.my.organizer.R;
import com.my.organizer.models.Expense;
import com.my.organizer.viewmodel.ExpenseViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditExpenseActivity extends AppCompatActivity {

    private EditText edtAmount, edtDescription, edtDate;
    private Button btnSave;
    private ExpenseViewModel expenseViewModel;
    private Date selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_expense);

        edtAmount = findViewById(R.id.edtAmount);
        edtDescription = findViewById(R.id.edtDescription);
        edtDate = findViewById(R.id.edtDate);
        btnSave = findViewById(R.id.btnSave);

        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);

        edtDate.setOnClickListener(view -> showDatePickerDialog());

        btnSave.setOnClickListener(view -> saveExpense());
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

    private void saveExpense() {
        String description = edtDescription.getText().toString().trim();
        String amountStr = edtAmount.getText().toString().trim();

        if (description.isEmpty() || amountStr.isEmpty() || selectedDate == null) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        Expense expense = new Expense(description, amount, selectedDate);
        expenseViewModel.insert(expense);
        Toast.makeText(this, "Expense saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}
