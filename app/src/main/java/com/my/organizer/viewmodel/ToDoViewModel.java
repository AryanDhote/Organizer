package com.my.organizer.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.my.organizer.database.ToDoRepository;
import com.my.organizer.models.ToDo;

import java.util.List;

public class ToDoViewModel extends AndroidViewModel {

    private ToDoRepository toDoRepository;
    private LiveData<List<ToDo>> allToDos;

    public ToDoViewModel(Application application) {
        super(application);
        toDoRepository = new ToDoRepository(application);
        allToDos = toDoRepository.getAllToDos();
    }

    public LiveData<List<ToDo>> getAllToDos() {
        return allToDos;
    }

    public void insert(ToDo toDo) {
        toDoRepository.insert(toDo);
    }

    public void update(ToDo toDo) {
        toDoRepository.update(toDo);
    }

    public void delete(ToDo toDo) {
        toDoRepository.delete(toDo);
    }

    public void deleteAllToDos() {
        toDoRepository.deleteAllToDos();
    }
}
