package com.my.organizer.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.my.organizer.database.ExpenseRepository;
import com.my.organizer.models.Expense;

import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {

    private ExpenseRepository expenseRepository;
    private LiveData<List<Expense>> allExpenses;

    public ExpenseViewModel(Application application) {
        super(application);
        expenseRepository = new ExpenseRepository(application);
        allExpenses = expenseRepository.getAllExpenses();
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public void insert(Expense expense) {
        expenseRepository.insert(expense);
    }

    public void update(Expense expense) {
        expenseRepository.update(expense);
    }

    public void delete(Expense expense) {
        expenseRepository.delete(expense);
    }

    public void deleteAllExpenses() {
        expenseRepository.deleteAllExpenses();
    }
}
