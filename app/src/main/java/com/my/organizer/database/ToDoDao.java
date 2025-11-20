package com.my.organizer.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.my.organizer.models.ToDo;

import java.util.List;

@Dao
public interface ToDoDao {

    @Insert
    void insert(ToDo todo);

    @Update
    void update(ToDo todo);

    @Delete
    void delete(ToDo todo);

    // Use the actual column name from your entity: dueDate
    @Query("SELECT * FROM todo_table ORDER BY dueDate ASC")
    LiveData<List<ToDo>> getAllToDos();

    @Query("SELECT COUNT(*) FROM todo_table")
    LiveData<Integer> getTotalToDoCount();

    @Query("SELECT * FROM todo_table WHERE id = :id")
    LiveData<ToDo> getToDoById(int id);

    // Compare dueDate with a timestamp (millis). We'll store Date as long via TypeConverter.
    @Query("SELECT * FROM todo_table WHERE dueDate < :currentTime")
    LiveData<List<ToDo>> getExpiredToDos(long currentTime);

    @Query("DELETE FROM todo_table")
    void deleteAllToDos();
}
