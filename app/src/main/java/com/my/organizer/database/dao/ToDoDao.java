package com.my.organizer.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.my.organizer.models.ToDo;
import java.util.List;

@Dao
public interface ToDoDao {
    @Insert
    void insertToDo(ToDo toDo);

    @Update
    void updateToDo(ToDo toDo);

    @Delete
    void deleteToDo(ToDo toDo);

    @Query("SELECT * FROM todos ORDER BY id DESC")
    LiveData<List<ToDo>> getAllToDos();
}
