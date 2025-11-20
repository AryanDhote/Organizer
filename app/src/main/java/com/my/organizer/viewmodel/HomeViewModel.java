package com.my.organizer.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.my.organizer.database.AppDatabase;
import com.my.organizer.database.ExpenseDao;
import com.my.organizer.database.ToDoDao;
import com.my.organizer.models.Expense;
import com.my.organizer.models.ToDo;

public class HomeViewModel extends AndroidViewModel {

    private LiveData<Integer> totalToDoCount;
    private LiveData<Integer> totalExpenseCount;
    private LiveData<Double> totalExpenseAmount;

    public HomeViewModel(Application application) {
        super(application);

        AppDatabase db = AppDatabase.getInstance(application);
        ToDoDao toDoDao = db.toDoDao();
        ExpenseDao expenseDao = db.expenseDao();

        totalToDoCount = toDoDao.getTotalToDoCount();
        totalExpenseCount = expenseDao.getTotalExpenseCount();
        totalExpenseAmount = expenseDao.getTotalExpenseAmount();
    }

    public LiveData<Integer> getTotalToDoCount() {
        return totalToDoCount;
    }

    public LiveData<Integer> getTotalExpenseCount() {
        return totalExpenseCount;
    }

    public LiveData<Double> getTotalExpenseAmount() {
        return totalExpenseAmount;
    }
}
