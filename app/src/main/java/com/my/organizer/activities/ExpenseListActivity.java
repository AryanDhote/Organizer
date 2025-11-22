package com.my.organizer.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.my.organizer.R;
import com.my.organizer.adapters.ExpenseAdapter;
import com.my.organizer.models.Expense;
import com.my.organizer.viewmodel.ExpenseViewModel;

public class ExpenseListActivity extends AppCompatActivity {

    private ExpenseViewModel expenseViewModel;
    private ExpenseAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        RecyclerView rv = findViewById(R.id.rv_expenses);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ExpenseAdapter();
        rv.setAdapter(adapter);

        adapter.setOnExpenseClickListener(new ExpenseAdapter.OnExpenseClickListener() {
            @Override
            public void onExpenseClick(Expense expense) {
                Intent i = new Intent(ExpenseListActivity.this, AddEditExpenseActivity.class);
                i.putExtra(AddEditExpenseActivity.EXTRA_EXPENSE, expense);
                startActivity(i);
            }
        });

        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        expenseViewModel.getAllExpenses().observe(this, expenses -> adapter.setExpenseList(expenses));
    }
}
