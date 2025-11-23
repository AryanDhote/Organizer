package com.my.organizer.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.my.organizer.database.AppDatabase;
import com.my.organizer.database.ToDoRepository;
import com.my.organizer.models.ToDo;
import com.my.organizer.utils.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for ToDo operations.
 * - Exposes LiveData<List<ToDo>> from repository
 * - insert / update / delete single items
 * - deleteBulk(List<ToDo>) uses repository.deleteByIds(...)
 * - deleteEvent posts Event<Boolean> to indicate success/failure of delete operations
 */
public class ToDoViewModel extends AndroidViewModel {

    private final ToDoRepository repository;
    private final LiveData<List<ToDo>> allToDos;
    private final LiveData<Integer> totalToDoCount;

    // One-shot event for delete success/failure notifications
    private final MutableLiveData<Event<Boolean>> deleteEvent = new MutableLiveData<>();

    public ToDoViewModel(@NonNull Application application) {
        super(application);
        repository = new ToDoRepository(application);
        allToDos = repository.getAllToDos();
        totalToDoCount = repository.getTotalToDoCount();
    }

    public LiveData<List<ToDo>> getAllToDos() {
        return allToDos;
    }

    public LiveData<Integer> getTotalToDoCount() {
        return totalToDoCount;
    }

    /**
     * Expose delete event LiveData so fragments/activities can show feedback.
     */
    public LiveData<Event<Boolean>> getDeleteEvent() {
        return deleteEvent;
    }

    // CRUD ops
    public void insert(ToDo toDo) {
        repository.insert(toDo);
    }

    public void update(ToDo toDo) {
        repository.update(toDo);
    }

    public void delete(ToDo toDo) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                repository.delete(toDo);
                deleteEvent.postValue(new Event<>(true));
            } catch (Exception ex) {
                deleteEvent.postValue(new Event<>(false));
            }
        });
    }

    /**
     * Bulk delete given list of ToDo objects by extracting their ids and calling repository.deleteByIds.
     * Posting a one-shot Event<Boolean> indicating success or failure.
     */
    public void deleteBulk(List<ToDo> todos) {
        if (todos == null || todos.isEmpty()) {
            deleteEvent.postValue(new Event<>(false));
            return;
        }
        final List<Integer> ids = new ArrayList<>();
        for (ToDo t : todos) {
            ids.add(t.getId());
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
