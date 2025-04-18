package com.my.organizer.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.my.organizer.database.AppDatabase;
import com.my.organizer.database.dao.ToDoDao;
import com.my.organizer.models.ToDo;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ToDoViewModel extends AndroidViewModel {
    private final ToDoDao toDoDao;
    private final LiveData<List<ToDo>> allToDos;
    private final ExecutorService executorService;

    public ToDoViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        toDoDao = db.toDoDao();
        allToDos = toDoDao.getAllToDos();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<ToDo>> getAllToDos() {
        return allToDos;
    }

    public void insertToDo(ToDo toDo) {
        executorService.execute(() -> toDoDao.insertToDo(toDo));
    }

    public void updateToDo(ToDo toDo) {
        executorService.execute(() -> toDoDao.updateToDo(toDo));
    }

    public void deleteToDo(ToDo toDo) {
        executorService.execute(() -> toDoDao.deleteToDo(toDo));
    }



}
