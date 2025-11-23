package com.my.organizer.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.my.organizer.database.AppDatabase;
import com.my.organizer.database.ExpenseRepository;
import com.my.organizer.models.Expense;
import com.my.organizer.utils.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for Expense operations.
 * - Exposes LiveData<List<Expense>> and summary LiveData (total amount, count)
 * - insert / update / delete single items
 * - deleteBulk(List<Expense>) uses repository.deleteByIds(...)
 * - deleteEvent posts Event<Boolean> for UI notification
 */
public class ExpenseViewModel extends AndroidViewModel {

    private final ExpenseRepository repository;
    private final LiveData<List<Expense>> allExpenses;
    private final LiveData<Double> totalExpenseAmount;
    private final LiveData<Integer> totalExpenseCount;

    private final MutableLiveData<Event<Boolean>> deleteEvent = new MutableLiveData<>();

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

    public LiveData<Event<Boolean>> getDeleteEvent() {
        return deleteEvent;
    }

    // CRUD ops
    public void insert(Expense e) {
        repository.insert(e);
    }

    public void update(Expense e) {
        repository.update(e);
    }

    public void delete(Expense e) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                repository.delete(e);
                deleteEvent.postValue(new Event<>(true));
            } catch (Exception ex) {
                deleteEvent.postValue(new Event<>(false));
            }
        });
    }

    /**
     * Bulk delete given list of Expense objects.
     */
    public void deleteBulk(List<Expense> expenses) {
        if (expenses == null || expenses.isEmpty()) {
            deleteEvent.postValue(new Event<>(false));
            return;
        }
        final List<Integer> ids = new ArrayList<>();
        for (Expense e : expenses) {
            ids.add(e.getId());
        }

        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                repository.deleteByIds(ids);
                deleteEvent.postValue(new Event<>(true));
            } catch (Exception ex) {
                deleteEvent.postValue(new Event<>(false));
            }
        });
    }
}
