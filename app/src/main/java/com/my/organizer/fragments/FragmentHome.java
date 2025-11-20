package com.my.organizer.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import com.my.organizer.R;
import com.my.organizer.viewmodel.HomeViewModel;

public class FragmentHome extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView todoCountTextView;
    private TextView expenseTotalTextView;
    private Button btnAddToDo;
    private Button btnAddExpense;

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bind views (IDs from your provided fragment_home.xml)
        todoCountTextView = view.findViewById(R.id.tv_todo_count);
        expenseTotalTextView = view.findViewById(R.id.tv_expense_total);
        btnAddToDo = view.findViewById(R.id.btn_add_todo);
        btnAddExpense = view.findViewById(R.id.btn_add_expense);

        // Initialize ViewModel
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Observe total ToDo count (Integer)
        homeViewModel.getTotalToDoCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer totalToDoCount) {
                // defensive: handle null
                int count = (totalToDoCount == null) ? 0 : totalToDoCount;
                todoCountTextView.setText("Total Tasks: " + count);
            }
        });

        // Observe total expense amount (Double)
        homeViewModel.getTotalExpenseAmount().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double totalExpenseAmount) {
                double amount = (totalExpenseAmount == null) ? 0.0 : totalExpenseAmount;
                // Format using Rupee symbol
                expenseTotalTextView.setText(String.format("Total Expenses: ₹%.2f", amount));
            }
        });

        // Button click hooks — currently no-op; host Activity/Fragment can set behavior
        btnAddToDo.setOnClickListener(v -> {
            // Example: start AddEditToDoActivity or notify host
            // Currently empty — replace with navigation or callback as needed.
        });

        btnAddExpense.setOnClickListener(v -> {
            // Example: start AddEditExpenseActivity or notify host
            // Currently empty — replace with navigation or callback as needed.
        });
    }
}
