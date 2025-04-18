package com.my.organizer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.my.organizer.R;
import com.my.organizer.adapters.ExpenseAdapter;
import com.my.organizer.models.Expense;
import java.util.ArrayList;
import java.util.List;

public class ExpenseListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ExpenseAdapter adapter;
    private List<Expense> expenseList;
    private FloatingActionButton fabAddExpense;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense_list, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewExpenses);
        fabAddExpense = view.findViewById(R.id.fabAddExpense);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize data (dummy for now)
        expenseList = new ArrayList<>();
        adapter = new ExpenseAdapter(expenseList);
        recyclerView.setAdapter(adapter);

        // Floating button to add new expense
        fabAddExpense.setOnClickListener(v -> {
            // TODO: Navigate to AddEditExpenseActivity
        });

        // Adding accessibility description for Floating Action Button
        fabAddExpense.setContentDescription(getString(R.string.add_expense));

        return view;
    }
}
