package com.my.organizer.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.my.organizer.models.Expense;
import java.util.List;

@Dao
public interface ExpenseDao {
    @Insert
    void insert(Expense expense);

    @Update
    void update(Expense expense);

    @Delete
    void delete(Expense expense);

    @Query("SELECT * FROM expenses ORDER BY id DESC")
    LiveData<List<Expense>> getAllExpenses();
}
