package com.my.organizer.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.my.organizer.R;
import com.my.organizer.adapters.ExpenseAdapter;
import com.my.organizer.models.Expense;
import com.my.organizer.viewmodel.ExpenseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ExpenseListFragment extends Fragment {

    public interface OnExpenseListActionListener {
        void onAddExpense();
        void onEditExpense(Expense expense);

        // ----------------------------
        // ExpenseListFragment.OnExpenseListActionListener
        // ----------------------------
        void onAddExpenseFromList();

        void onEditExpenseFromList(Expense expense);
    }

    private OnExpenseListActionListener listener;
    private ExpenseViewModel expenseViewModel;
    private ExpenseAdapter adapter;

    public ExpenseListFragment() { /* required empty */ }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnExpenseListActionListener) {
            listener = (OnExpenseListActionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnExpenseListActionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expense_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = view.findViewById(R.id.rv_expenses_fragment);
        FloatingActionButton fabAdd = view.findViewById(R.id.fab_add_expense);

        adapter = new ExpenseAdapter();
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        adapter.setOnExpenseClickListener(expense -> {
            if (listener != null) listener.onEditExpense(expense);
        });

        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        expenseViewModel.getAllExpenses().observe(getViewLifecycleOwner(), expenses -> {
            adapter.setExpenseList(expenses);
        });

        fabAdd.setOnClickListener(v -> {
            if (listener != null) listener.onAddExpense();
        });
    }
}
