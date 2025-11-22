package com.my.organizer.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.my.organizer.database.ExpenseRepository;
import com.my.organizer.models.Expense;

import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {

    private final ExpenseRepository repository;
    private final LiveData<List<Expense>> allExpenses;
    private final LiveData<Double> totalExpenseAmount;
    private final LiveData<Integer> totalExpenseCount;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        repository = new ExpenseRepository(application);
        allExpenses = repository.getAllExpenses();
        totalExpenseAmount = repository.getTotalExpenseAmount();
        totalExpenseCount = repository.getTotalExpenseCount();
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public LiveData<Double> getTotalExpenseAmount() {
        return totalExpenseAmount;
    }

    public LiveData<Integer> getTotalExpenseCount() {
        return totalExpenseCount;
    }

    public LiveData<Expense> getExpenseById(int id) {
        return repository.getExpenseById(id);
    }

    public void insert(Expense expense) {
        repository.insert(expense);
    }

    public void update(Expense expense) {
        repository.update(expense);
    }

    public void delete(Expense expense) {
        repository.delete(expense);
    }

    public void deleteAllExpenses() {
        repository.deleteAllExpenses();
    }
}
