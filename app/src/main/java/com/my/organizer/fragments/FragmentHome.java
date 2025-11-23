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
import java.util.List;

public class FragmentHome extends Fragment {

    // Views
    private TextView tvTodoCount, tvExpenseTotal;
    private MaterialButton btnAddTodo, btnAddExpense, btnShowTodos, btnShowExpenses;
    private RecyclerView rvTodoPreview, rvExpensePreview;

    // Adapters & ViewModels
    private ToDoAdapter toDoAdapter;
    private ExpenseAdapter expenseAdapter;
    private HomeViewModel homeViewModel;
    private ToDoViewModel toDoViewModel;
    private ExpenseViewModel expenseViewModel;

    // Host callbacks
    public interface OnHomeActionListener {
        void onAddToDo();
        void onAddExpense();
        void onShowAllToDos();
        void onShowAllExpenses();

        void onEditToDo(ToDo toDo);
        void onEditExpense(Expense expense);
    }
    private OnHomeActionListener listener;

    public FragmentHome() { }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnHomeActionListener) {
            listener = (OnHomeActionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentHome.OnHomeActionListener");
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

        toDoAdapter = new ToDoAdapter(requireContext());
        expenseAdapter = new ExpenseAdapter();

        // Home preview: disable selection and overflow. Taps open edit.
        toDoAdapter.setSelectionEnabled(false);
        toDoAdapter.setShowOverflow(false);
        expenseAdapter.setSelectionEnabled(false);
        expenseAdapter.setShowOverflow(false);

        toDoAdapter.setOnToDoClickListener(toDo -> {
            if (listener != null) listener.onEditToDo(toDo);
        });
        expenseAdapter.setOnExpenseClickListener(expense -> {
            if (listener != null) listener.onEditExpense(expense);
        });

        rvTodoPreview.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvTodoPreview.setAdapter(toDoAdapter);

        rvExpensePreview.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvExpensePreview.setAdapter(expenseAdapter);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        toDoViewModel = new ViewModelProvider(requireActivity()).get(ToDoViewModel.class);
        expenseViewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);

        homeViewModel.getTotalToDoCount().observe(getViewLifecycleOwner(), count -> {
            int c = (count == null) ? 0 : count;
            tvTodoCount.setText("Total Tasks: " + c);
        });

        homeViewModel.getTotalExpenseAmount().observe(getViewLifecycleOwner(), amount -> {
            double a = (amount == null) ? 0.0 : amount;
            tvExpenseTotal.setText(String.format("Total Expenses: ₹%.2f", a));
        });

        // previews (limit to 3)
        toDoViewModel.getAllToDos().observe(getViewLifecycleOwner(), toDos -> {
            List<ToDo> preview = takePreview(toDos, 3);
            toDoAdapter.setToDoList(preview);
            // ensure selection cleared and preview is read-only
            toDoAdapter.clearSelection();
            toDoAdapter.setSelectionEnabled(false);
        });

        expenseViewModel.getAllExpenses().observe(getViewLifecycleOwner(), expenses -> {
            List<Expense> preview = takePreview(expenses, 3);
            expenseAdapter.setExpenseList(preview);
            expenseAdapter.clearSelection();
            expenseAdapter.setSelectionEnabled(false);
        });

        btnAddTodo.setOnClickListener(v -> { if (listener != null) listener.onAddToDo(); });
        btnAddExpense.setOnClickListener(v -> { if (listener != null) listener.onAddExpense(); });

        btnShowTodos.setOnClickListener(v -> { if (listener != null) listener.onShowAllToDos(); });
        btnShowExpenses.setOnClickListener(v -> { if (listener != null) listener.onShowAllExpenses(); });
    }

    private <T> List<T> takePreview(List<T> list, int limit) {
        if (list == null) return new ArrayList<>();
        if (list.size() <= limit) return list;
        return new ArrayList<>(list.subList(0, limit));
    }
}
