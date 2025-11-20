package com.my.organizer.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.my.organizer.R;
import com.my.organizer.adapters.ExpenseAdapter;
import com.my.organizer.models.Expense;
import com.my.organizer.viewmodel.ExpenseViewModel;
import java.util.List;

public class ExpenseListActivity extends AppCompatActivity {

    private ExpenseViewModel expenseViewModel;
    private ExpenseAdapter expenseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        RecyclerView recyclerView = findViewById(R.id.recycler_expenses);
        MaterialButton addBtn = findViewById(R.id.btn_add_expense);

        expenseAdapter = new ExpenseAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(expenseAdapter);

        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        expenseViewModel.getAllExpenses().observe(this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(List<Expense> expenses) {
                expenseAdapter.setExpenseList(expenses);
            }
        });

        addBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditExpenseActivity.class);
            startActivity(intent);
        });

        // Set click listener for the adapter to handle item clicks
        expenseAdapter.setOnExpenseClickListener(expense -> {
            Intent intent = new Intent(this, AddEditExpenseActivity.class);
            intent.putExtra("expense", expense);
            startActivity(intent);
        });
    }
}
