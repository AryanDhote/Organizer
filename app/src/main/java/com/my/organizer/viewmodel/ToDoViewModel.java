package com.my.organizer.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.my.organizer.database.ToDoRepository;
import com.my.organizer.models.ToDo;

import java.util.List;

public class ToDoViewModel extends AndroidViewModel {

    private final ToDoRepository repository;
    private final LiveData<List<ToDo>> allToDos;
    private final LiveData<Integer> totalToDoCount;

    public ToDoViewModel(@NonNull Application application) {
        super(application);
        repository = new ToDoRepository(application);
        allToDos = repository.getAllToDos();
        totalToDoCount = repository.getTotalToDoCount();
    }

    public LiveData<List<ToDo>> getAllToDos() {
        return allToDos;
    }

    public LiveData<Integer> getTotalToDoCount() {
        return totalToDoCount;
    }

    public LiveData<ToDo> getToDoById(int id) {
        return repository.getToDoById(id);
    }

    public LiveData<List<ToDo>> getExpiredToDos(long currentTimeMillis) {
        return repository.getExpiredToDos(currentTimeMillis);
    }

    public void insert(ToDo toDo) {
        repository.insert(toDo);
    }

    public void update(ToDo toDo) {
        repository.update(toDo);
    }

    public void delete(ToDo toDo) {
        repository.delete(toDo);
    }

    public void deleteAllToDos() {
        repository.deleteAllToDos();
    }
}
