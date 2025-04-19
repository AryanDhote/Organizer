package com.my.organizer.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.my.organizer.database.AppDatabase;
import com.my.organizer.models.Expense;
import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {
    private final AppDatabase db;
    private final LiveData<List<Expense>> allExpenses;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
        allExpenses = db.expenseDao().getAllExpenses();
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public void insert(Expense expense) {
        AppDatabase.databaseWriteExecutor.execute(() -> db.expenseDao().insert(expense));
    }

    public void update(Expense expense) {
        AppDatabase.databaseWriteExecutor.execute(() -> db.expenseDao().update(expense));
    }

    public void delete(Expense expense) {
        AppDatabase.databaseWriteExecutor.execute(() -> db.expenseDao().delete(expense));
    }
}
