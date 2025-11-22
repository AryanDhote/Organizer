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
import com.my.organizer.adapters.ToDoAdapter;
import com.my.organizer.models.ToDo;
import com.my.organizer.viewmodel.ToDoViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ToDoListFragment extends Fragment {

    public interface OnToDoListActionListener {
        void onAddToDo();
        void onEditToDo(ToDo toDo);

        // ----------------------------
        // ToDoListFragment.OnToDoListActionListener
        // ----------------------------
        // These are the same actions as above; delegate to the same methods
        void onAddToDoFromList();

        void onEditToDoFromList(ToDo toDo);
    }

    private OnToDoListActionListener listener;
    private ToDoViewModel toDoViewModel;
    private ToDoAdapter adapter;

    public ToDoListFragment() { /* required empty */ }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnToDoListActionListener) {
            listener = (OnToDoListActionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnToDoListActionListener");
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
        return inflater.inflate(R.layout.fragment_todo_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = view.findViewById(R.id.rv_todos_fragment);
        FloatingActionButton fabAdd = view.findViewById(R.id.fab_add_todo);

        adapter = new ToDoAdapter(requireContext());
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        // click -> edit
        adapter.setOnToDoClickListener(toDo -> {
            if (listener != null) listener.onEditToDo(toDo);
        });

        toDoViewModel = new ViewModelProvider(this).get(ToDoViewModel.class);
        toDoViewModel.getAllToDos().observe(getViewLifecycleOwner(), todos -> {
            adapter.setToDoList(todos);
        });

        fabAdd.setOnClickListener(v -> {
            if (listener != null) listener.onAddToDo();
        });
    }
}
