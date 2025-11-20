package com.my.organizer.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.my.organizer.models.ToDo;

import java.util.List;

public class ToDoRepository {

    private final ToDoDao toDoDao;
    private final LiveData<List<ToDo>> allToDos;
    private final LiveData<Integer> totalToDoCount;

    public ToDoRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        toDoDao = db.toDoDao();
        allToDos = toDoDao.getAllToDos();
        totalToDoCount = toDoDao.getTotalToDoCount();
    }

    public LiveData<List<ToDo>> getAllToDos() {
        return allToDos;
    }

    public LiveData<Integer> getTotalToDoCount() {
        return totalToDoCount;
    }

    public LiveData<ToDo> getToDoById(int id) {
        return toDoDao.getToDoById(id);
    }

    public LiveData<List<ToDo>> getExpiredToDos(long currentTimeMillis) {
        return toDoDao.getExpiredToDos(currentTimeMillis);
    }

    public void insert(ToDo todo) {
        AppDatabase.databaseWriteExecutor.execute(() -> toDoDao.insert(todo));
    }

    public void update(ToDo todo) {
        AppDatabase.databaseWriteExecutor.execute(() -> toDoDao.update(todo));
    }

    public void delete(ToDo todo) {
        AppDatabase.databaseWriteExecutor.execute(() -> toDoDao.delete(todo));
    }
}
