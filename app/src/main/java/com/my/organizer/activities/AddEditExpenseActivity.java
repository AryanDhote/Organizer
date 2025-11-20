package com.my.organizer.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.my.organizer.R;
import com.my.organizer.models.Expense;

public class AddEditExpenseActivity extends AppCompatActivity {

    private TextView expenseDetailsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_expense);

        expenseDetailsTextView = findViewById(R.id.expense_details_text_view);

        // Retrieve the Expense object passed via Intent
        Expense expense = (Expense) getIntent().getSerializableExtra("expense");

        if (expense != null) {
            // Display the details of the expense
            expenseDetailsTextView.setText("Amount: ₹" + expense.getAmount() + "\n" +
                    "Type: " + expense.getType().name() + "\n" +
                    "Note: " + expense.getNote() + "\n" +
                    "Date: " + expense.getDate());
        }
    }
}
