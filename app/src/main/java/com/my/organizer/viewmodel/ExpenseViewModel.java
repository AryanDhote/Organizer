package com.my.organizer.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.my.organizer.database.AppDatabase;
import com.my.organizer.database.dao.ExpenseDao;
import com.my.organizer.models.Expense;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExpenseViewModel extends AndroidViewModel {
    private ExpenseDao expenseDao;
    private LiveData<List<Expense>> allExpenses;
    private ExecutorService executorService;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        expenseDao = db.expenseDao();
        allExpenses = expenseDao.getAllExpenses();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public void insert(Expense expense) {
        executorService.execute(() -> expenseDao.insert(expense));
    }

    public void update(Expense expense) {
        executorService.execute(() -> expenseDao.update(expense));
    }

    public void delete(Expense expense) {
        executorService.execute(() -> expenseDao.delete(expense));
    }
}
