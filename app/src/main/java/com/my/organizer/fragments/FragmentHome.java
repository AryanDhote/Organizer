package com.my.organizer.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.my.organizer.R;
import com.my.organizer.viewmodel.HomeViewModel;

public class FragmentHome extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView todoCountTextView;
    private TextView expenseCountTextView;
    private TextView expenseTotalTextView;

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize UI components
        todoCountTextView = view.findViewById(R.id.text_todo_count);
        expenseCountTextView = view.findViewById(R.id.text_expense_count);
        expenseTotalTextView = view.findViewById(R.id.text_expense_total);

        // Initialize ViewModel
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Observing data from ViewModel
        homeViewModel.getTotalToDoCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer totalToDoCount) {
                todoCountTextView.setText(String.valueOf(totalToDoCount));
            }
        });

        homeViewModel.getTotalExpenseCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer totalExpenseCount) {
                expenseCountTextView.setText(String.valueOf(totalExpenseCount));
            }
        });

        homeViewModel.getTotalExpenseAmount().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double totalExpenseAmount) {
                expenseTotalTextView.setText(String.format("$%.2f", totalExpenseAmount));
            }
        });

        return view;
    }
}
