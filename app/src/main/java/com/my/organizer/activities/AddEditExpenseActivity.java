package com.my.organizer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.my.organizer.R;
import com.my.organizer.models.Expense;
import com.my.organizer.viewmodel.ExpenseViewModel;

public class AddEditExpenseActivity extends AppCompatActivity {

    private EditText editTextAmount, editTextDescription, editTextCategory;
    private Button buttonSave;
    private ExpenseViewModel expenseViewModel;
    private int expenseId = -1;  // Default -1 means it's a new expense

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_expense);

        editTextAmount = findViewById(R.id.editTextAmount);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextCategory = findViewById(R.id.editTextCategory);
        buttonSave = findViewById(R.id.buttonSaveExpense);

        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);

        Intent intent = getIntent();
        if (intent.hasExtra("expense_id")) {
            setTitle("Edit Expense");
            expenseId = intent.getIntExtra("expense_id", -1);
            editTextAmount.setText(intent.getStringExtra("expense_amount"));
            editTextDescription.setText(intent.getStringExtra("expense_description"));
            editTextCategory.setText(intent.getStringExtra("expense_category"));
        } else {
            setTitle("Add Expense");
        }

        buttonSave.setOnClickListener(view -> saveExpense());
    }

    private void saveExpense() {
        String amountText = editTextAmount.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();

        // Check if fields are empty
        if (amountText.isEmpty() || description.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate numeric input
        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount format", Toast.LENGTH_SHORT).show();
            return;
        }

        Expense expense = new Expense(description,amount,  category);

        if (expenseId != -1) {
            expense.setId(expenseId);
            expenseViewModel.update(expense);
        } else {
            expenseViewModel.insert(expense);
        }

        Toast.makeText(this, "Expense saved", Toast.LENGTH_SHORT).show();
        finish();
    }

}
