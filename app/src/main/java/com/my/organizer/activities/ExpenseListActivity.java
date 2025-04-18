package com.my.organizer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.my.organizer.R;
import com.my.organizer.adapters.ExpenseAdapter;
import com.my.organizer.models.Expense;
import com.my.organizer.viewmodel.ExpenseViewModel;
import java.util.List;

public class ExpenseListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExpenseAdapter expenseAdapter;
    private ExpenseViewModel expenseViewModel;
    private FloatingActionButton fabAddExpense;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        recyclerView = findViewById(R.id.recyclerViewExpenses);
        fabAddExpense = findViewById(R.id.fabAddExpense);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        expenseAdapter = new ExpenseAdapter();
        recyclerView.setAdapter(expenseAdapter);

        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        expenseViewModel.getAllExpenses().observe(this, this::updateUI);

        expenseAdapter.setOnItemClickListener(expense -> {
            Intent intent = new Intent(ExpenseListActivity.this, AddEditExpenseActivity.class);
            intent.putExtra("expense_id", expense.getId());
            startActivity(intent);
        });

        fabAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(ExpenseListActivity.this, AddEditExpenseActivity.class);
            startActivity(intent);
        });
    }

    private void updateUI(List<Expense> expenses) {
        if (expenses != null) {
            expenseAdapter.updateList(expenses);
        } else {
            Toast.makeText(this, "No expenses found", Toast.LENGTH_SHORT).show();
        }
    }
}
