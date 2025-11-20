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
import com.my.organizer.adapters.ToDoAdapter;
import com.my.organizer.models.ToDo;
import com.my.organizer.viewmodel.ToDoViewModel;
import com.my.organizer.activities.AddEditToDoActivity;

import java.util.List;

public class ToDoListFragment extends Fragment {

    private ToDoViewModel toDoViewModel;
    private ToDoAdapter toDoAdapter;

    public ToDoListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_todos);
        MaterialButton addBtn = view.findViewById(R.id.btn_add_todo);

        // Set up adapter & RecyclerView
        toDoAdapter = new ToDoAdapter(requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(toDoAdapter);

        // ViewModel & LiveData
        toDoViewModel = new ViewModelProvider(this).get(ToDoViewModel.class);
        toDoViewModel.getAllToDos().observe(getViewLifecycleOwner(), new Observer<List<ToDo>>() {
            @Override
            public void onChanged(List<ToDo> toDos) {
                toDoAdapter.setToDoList(toDos);
            }
        });

        // Add button opens AddEditToDoActivity
        addBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddEditToDoActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
