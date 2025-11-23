package com.my.organizer.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.my.organizer.models.Expense;
import com.my.organizer.models.ToDo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {ToDo.class, Expense.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "organizer_db";
    private static volatile AppDatabase instance;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    public abstract ToDoDao toDoDao();
    public abstract ExpenseDao expenseDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
