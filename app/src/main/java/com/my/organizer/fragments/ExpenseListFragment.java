package com.my.organizer.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.button.MaterialButton;
import com.my.organizer.R;
import com.my.organizer.adapters.ExpenseAdapter;
import com.my.organizer.models.Expense;
import com.my.organizer.viewmodel.ExpenseViewModel;
import com.my.organizer.activities.AddEditExpenseActivity;
import java.util.List;

public class ExpenseListFragment extends Fragment {

    private ExpenseViewModel expenseViewModel;
    private ExpenseAdapter expenseAdapter;

    public ExpenseListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_expenses);
        MaterialButton addBtn = view.findViewById(R.id.btn_add_expense);

        // Set up RecyclerView and Adapter
        expenseAdapter = new ExpenseAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(expenseAdapter);

        // ViewModel for managing Expense items
        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        expenseViewModel.getAllExpenses().observe(getViewLifecycleOwner(), new Observer<List<Expense>>() {
            @Override
            public void onChanged(List<Expense> expenses) {
                expenseAdapter.setExpenseList(expenses); // Update adapter with new list
            }
        });

        // Add button opens the Add/Edit Expense activity
        addBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddEditExpenseActivity.class);
            startActivity(intent);
        });

        // Set click listener for the adapter to handle item clicks
        expenseAdapter.setOnExpenseClickListener(expense -> {
            Intent intent = new Intent(getActivity(), AddEditExpenseActivity.class);
            intent.putExtra("expense", expense);
            startActivity(intent);
        });

        return view;
    }
}
