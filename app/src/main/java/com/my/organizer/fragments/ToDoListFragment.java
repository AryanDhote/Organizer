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
import com.my.organizer.adapters.ToDoAdapter;
import com.my.organizer.models.ToDo;

import java.util.List;

public class ToDoListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ToDoAdapter toDoAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_list, container, false);

        recyclerView = view.findViewById(R.id.todoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        toDoAdapter = new ToDoAdapter(getActivity()); // pass context properly
        recyclerView.setAdapter(toDoAdapter);

        view.findViewById(R.id.fabAddToDo).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToAddToDo();
            }
        });

        return view;
    }

    public void updateToDoList(List<ToDo> toDoList) {
        if (toDoAdapter != null) {
            toDoAdapter.submitList(toDoList);
        }
    }
}
