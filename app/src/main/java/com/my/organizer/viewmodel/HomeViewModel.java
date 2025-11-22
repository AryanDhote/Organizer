package com.my.organizer.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.my.organizer.database.ExpenseRepository;
import com.my.organizer.database.ToDoRepository;

public class HomeViewModel extends AndroidViewModel {

    private final ToDoRepository toDoRepository;
    private final ExpenseRepository expenseRepository;

    // Exposed LiveData for Home UI
    private final LiveData<Integer> totalToDoCount;
    private final LiveData<Double> totalExpenseAmount;
    private final LiveData<Integer> totalExpenseCount;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        toDoRepository = new ToDoRepository(application);
        expenseRepository = new ExpenseRepository(application);

        totalToDoCount = toDoRepository.getTotalToDoCount();
        totalExpenseAmount = expenseRepository.getTotalExpenseAmount();
        totalExpenseCount = expenseRepository.getTotalExpenseCount();
    }

    public LiveData<Integer> getTotalToDoCount() {
        return totalToDoCount;
    }

    public LiveData<Double> getTotalExpenseAmount() {
        return totalExpenseAmount;
    }

    public LiveData<Integer> getTotalExpenseCount() {
        return totalExpenseCount;
    }
}
