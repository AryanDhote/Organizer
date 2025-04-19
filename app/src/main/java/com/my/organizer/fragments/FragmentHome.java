package com.my.organizer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.card.MaterialCardView;  // Ensure correct import

import androidx.fragment.app.Fragment;

import com.my.organizer.R;
import com.my.organizer.activities.MainActivity;

public class FragmentHome extends Fragment {

    private MaterialCardView cardToDo, cardExpense;  // Use MaterialCardView instead of CardView

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the MaterialCardView objects
        cardToDo = view.findViewById(R.id.cardToDo);
        cardExpense = view.findViewById(R.id.cardExpense);

        // Navigate to ToDo list when cardToDo is clicked
        cardToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToToDoList();
                }
            }
        });

        // Navigate to Expense list when cardExpense is clicked
        cardExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToExpenseList();
                }
            }
        });

        return view;
    }
}
