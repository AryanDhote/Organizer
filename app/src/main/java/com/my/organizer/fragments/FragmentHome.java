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
import com.my.organizer.adapters.ToDoAdapter;
import com.my.organizer.models.Expense;
import com.my.organizer.models.ToDo;
import com.my.organizer.viewmodel.ExpenseViewModel;
import com.my.organizer.viewmodel.HomeViewModel;
import com.my.organizer.viewmodel.ToDoViewModel;
import com.google.android.material.button.MaterialButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentHome extends Fragment {

    private TextView tvTodoCount, tvExpenseTotal;
    private MaterialButton btnAddTodo, btnAddExpense, btnShowTodos, btnShowExpenses;
    private RecyclerView rvTodoPreview, rvExpensePreview;

    private ToDoAdapter toDoAdapter;
    private ExpenseAdapter expenseAdapter;

    private HomeViewModel homeViewModel;
    private ToDoViewModel toDoViewModel;
    private ExpenseViewModel expenseViewModel;

    public interface OnHomeActionListener {
        void onAddToDo();
        void onAddExpense();
        void onShowAllToDos();
        void onShowAllExpenses();
        void onEditToDo(ToDo toDo);
        void onEditExpense(Expense expense);
    }

    private OnHomeActionListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnHomeActionListener) {
            listener = (OnHomeActionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnHomeActionListener");
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        tvTodoCount = view.findViewById(R.id.tv_todo_count);
        tvExpenseTotal = view.findViewById(R.id.tv_expense_total);

        btnAddTodo = view.findViewById(R.id.btn_add_todo);
        btnAddExpense = view.findViewById(R.id.btn_add_expense);
        btnShowTodos = view.findViewById(R.id.btn_show_todos);
        btnShowExpenses = view.findViewById(R.id.btn_show_expenses);

        rvTodoPreview = view.findViewById(R.id.rv_todo_preview);
        rvExpensePreview = view.findViewById(R.id.rv_expense_preview);

        // ✅ INIT ADAPTERS
        toDoAdapter = new ToDoAdapter();
        expenseAdapter = new ExpenseAdapter();

        // ✅ DISABLE SELECTION MODE (VERY IMPORTANT)
        toDoAdapter.setSelectionEnabled(false);
        toDoAdapter.setShowOverflow(false);

        expenseAdapter.setSelectionEnabled(false);
        expenseAdapter.setShowOverflow(false);

        // ✅ CLEAR ANY PREVIOUS SELECTION
        toDoAdapter.clearSelection();
        expenseAdapter.clearSelection();

        // ✅ DISABLE LONG PRESS IN HOME
        toDoAdapter.setOnToDoLongClickListener(null);
        expenseAdapter.setOnExpenseLongClickListener(null);

        // ✅ CLICK -> EDIT
        toDoAdapter.setOnToDoClickListener(toDo -> {
            if (listener != null) listener.onEditToDo(toDo);
        });

        expenseAdapter.setOnExpenseClickListener(expense -> {
            if (listener != null) listener.onEditExpense(expense);
        });

        // ✅ SETUP RECYCLERS
        rvTodoPreview.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvTodoPreview.setAdapter(toDoAdapter);

        rvExpensePreview.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvExpensePreview.setAdapter(expenseAdapter);

        // ✅ VIEWMODELS
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        toDoViewModel = new ViewModelProvider(requireActivity()).get(ToDoViewModel.class);
        expenseViewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);

        // ✅ TOTAL COUNTS
        homeViewModel.getTotalToDoCount().observe(getViewLifecycleOwner(), count -> {
            tvTodoCount.setText("Total Tasks: " + (count == null ? 0 : count));
        });

        homeViewModel.getTotalExpenseAmount().observe(getViewLifecycleOwner(), amount -> {
            tvExpenseTotal.setText(String.format("Total Expenses: ₹%.2f", amount == null ? 0 : amount));
        });

        // ✅ SHOW ONLY 2 LATEST TODOS
        toDoViewModel.getAllToDos().observe(getViewLifecycleOwner(), list -> {
            toDoAdapter.setToDoList(getLatestItems(list, 2));
        });

        // ✅ SHOW ONLY 2 LATEST EXPENSES
        expenseViewModel.getAllExpenses().observe(getViewLifecycleOwner(), list -> {
            expenseAdapter.setExpenseList(getLatestItems(list, 2));
        });

        // ✅ BUTTON ACTIONS
        btnAddTodo.setOnClickListener(v -> {
            if (listener != null) listener.onAddToDo();
        });

        btnAddExpense.setOnClickListener(v -> {
            if (listener != null) listener.onAddExpense();
        });

        btnShowTodos.setOnClickListener(v -> {
            if (listener != null) listener.onShowAllToDos();
        });

        btnShowExpenses.setOnClickListener(v -> {
            if (listener != null) listener.onShowAllExpenses();
        });
    }

    // ✅ GET LATEST ITEMS (REVERSED)
    private <T> List<T> getLatestItems(List<T> list, int limit) {
        if (list == null || list.isEmpty()) return new ArrayList<>();

        List<T> reversed = new ArrayList<>(list);
        Collections.reverse(reversed);

        if (reversed.size() <= limit) return reversed;

        return reversed.subList(0, limit);
    }
}