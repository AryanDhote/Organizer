package com.my.organizer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.my.organizer.R;
import com.my.organizer.activities.AddEditToDoActivity;
import com.my.organizer.adapters.ToDoAdapter;
import com.my.organizer.models.ToDo;
import com.my.organizer.viewmodel.ToDoViewModel;
import java.util.List;

public class ToDoListFragment extends Fragment {
    private ToDoViewModel toDoViewModel;
    private ToDoAdapter adapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Log.d("ToDoListFragment", "RecyclerView ID: " + recyclerView);
        // Inflate the correct layout
        View view = inflater.inflate(R.layout.fragment_todo_list, container, false);

        // Ensure IDs match XML
        recyclerView = view.findViewById(R.id.recyclerViewToDos);
        FloatingActionButton fabAddToDo = view.findViewById(R.id.fabAddToDo);

        if (recyclerView == null) {
            throw new NullPointerException("RecyclerView is null. Check if the correct ID is in fragment_todo_list.xml.");
        }

        // Set RecyclerView properties
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        // Set Adapter
        adapter = new ToDoAdapter();
        recyclerView.setAdapter(adapter);

        // ViewModel
        toDoViewModel = new ViewModelProvider(this).get(ToDoViewModel.class);
        toDoViewModel.getAllToDos().observe(getViewLifecycleOwner(), toDos -> adapter.submitList(toDos));

        // FAB Click Listener
        fabAddToDo.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddEditToDoActivity.class);
            startActivity(intent);
        });

        return view;
    }

}
