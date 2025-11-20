package com.my.organizer.database;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.my.organizer.models.Expense;

import java.util.List;

public class ExpenseRepository {

    private final ExpenseDao expenseDao;
    private final LiveData<List<Expense>> allExpenses;
    private final LiveData<Double> totalExpenseAmount;

    public ExpenseRepository(Application application) {
        // Use the correct method name
        AppDatabase db = AppDatabase.getInstance(application);
        expenseDao = db.expenseDao();
        allExpenses = expenseDao.getAllExpenses();
        totalExpenseAmount = expenseDao.getTotalExpenseAmount();
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public LiveData<Double> getTotalExpenseAmount() {
        return totalExpenseAmount;
    }

    public LiveData<Expense> getExpenseById(int id) {
        return expenseDao.getExpenseById(id);
    }

    public void insert(Expense expense) {
        AppDatabase.databaseWriteExecutor.execute(() -> expenseDao.insert(expense));
    }

    public void update(Expense expense) {
        AppDatabase.databaseWriteExecutor.execute(() -> expenseDao.update(expense));
    }

    public void delete(Expense expense) {
        AppDatabase.databaseWriteExecutor.execute(() -> expenseDao.delete(expense));
    }

    public void deleteAllExpenses() {
        AppDatabase.databaseWriteExecutor.execute(() -> expenseDao.deleteAllExpenses());
    }
}
