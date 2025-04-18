package com.my.organizer.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.my.organizer.database.dao.ExpenseDao;
import com.my.organizer.database.dao.ToDoDao;
import com.my.organizer.models.Expense;
import com.my.organizer.models.ToDo;

@Database(entities = {Expense.class, ToDo.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract ExpenseDao expenseDao();
    public abstract ToDoDao toDoDao();

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
