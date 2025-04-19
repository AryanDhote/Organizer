package com.my.organizer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.my.organizer.R;
import com.my.organizer.activities.MainActivity;
import com.my.organizer.adapters.ExpenseAdapter;
import com.my.organizer.models.Expense;

import java.util.List;

public class ExpenseListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ExpenseAdapter expenseAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense_list, container, false);

        recyclerView = view.findViewById(R.id.expenseRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        expenseAdapter = new ExpenseAdapter(getActivity()); // Fixed: pass context to adapter
        recyclerView.setAdapter(expenseAdapter);

        view.findViewById(R.id.fabAddExpense).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToAddExpense();
            }
        });

        return view;
    }

    public void updateExpenseList(List<Expense> expenseList) {
        if (expenseAdapter != null) {
            expenseAdapter.submitList(expenseList);
        }
    }
}
