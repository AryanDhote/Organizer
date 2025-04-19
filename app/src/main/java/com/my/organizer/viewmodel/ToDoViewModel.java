package com.my.organizer.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.my.organizer.database.AppDatabase;
import com.my.organizer.models.ToDo;
import java.util.List;

public class ToDoViewModel extends AndroidViewModel {
    private final AppDatabase db;
    private final LiveData<List<ToDo>> allToDos;

    public ToDoViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
        allToDos = db.toDoDao().getAllToDos();
    }

    public LiveData<List<ToDo>> getAllToDos() {
        return allToDos;
    }

    public void insert(ToDo toDo) {
        AppDatabase.databaseWriteExecutor.execute(() -> db.toDoDao().insertToDo(toDo));
    }

    public void update(ToDo toDo) {
        AppDatabase.databaseWriteExecutor.execute(() -> db.toDoDao().updateToDo(toDo));
    }

    public void delete(ToDo toDo) {
        AppDatabase.databaseWriteExecutor.execute(() -> db.toDoDao().deleteToDo(toDo));
    }
}
