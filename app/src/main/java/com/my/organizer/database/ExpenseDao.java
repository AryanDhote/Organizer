package com.my.organizer.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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

    @Query("SELECT * FROM expense_table ORDER BY date ASC")
    LiveData<List<Expense>> getAllExpenses();

    @Query("SELECT SUM(amount) FROM expense_table")
    LiveData<Double> getTotalExpenseAmount();

    @Query("SELECT COUNT(*) FROM expense_table")
    LiveData<Integer> getTotalExpenseCount();

    // NEW: fetch one Expense by its primary key
    @Query("SELECT * FROM expense_table WHERE id = :id")
    LiveData<Expense> getExpenseById(int id);
}
