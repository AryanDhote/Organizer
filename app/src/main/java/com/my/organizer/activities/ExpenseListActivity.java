package com.my.organizer.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.my.organizer.R;
import com.my.organizer.adapters.ExpenseAdapter;
import com.my.organizer.viewmodel.ExpenseViewModel;

public class ExpenseListActivity extends AppCompatActivity {

    private ExpenseViewModel expenseViewModel;
    private ExpenseAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewExpenses);
        FloatingActionButton fabAddExpense = findViewById(R.id.fabAddExpense);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new ExpenseAdapter(this); // ✅ Pass context to adapter
        recyclerView.setAdapter(adapter);

        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        expenseViewModel.getAllExpenses().observe(this, expenses -> adapter.submitList(expenses));

        fabAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(ExpenseListActivity.this, AddEditExpenseActivity.class);
            startActivity(intent);
        });
    }
}
