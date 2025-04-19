package com.my.organizer.database;

import android.content.Context;
import androidx.room.*;
import com.my.organizer.models.Expense;
import com.my.organizer.models.ToDo;
import com.my.organizer.database.dao.ExpenseDao;
import com.my.organizer.database.dao.ToDoDao;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Expense.class, ToDo.class}, version = 1,exportSchema = false)
@TypeConverters(com.my.organizer.utils.Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase instance;
    public abstract ExpenseDao expenseDao();
    public abstract ToDoDao toDoDao();

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "organizer_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
