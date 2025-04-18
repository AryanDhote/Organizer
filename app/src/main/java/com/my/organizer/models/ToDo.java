package com.my.organizer.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "todos")
public class ToDo {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String task;
    private boolean completed;

    // Corrected constructor
    public ToDo(String task, boolean completed) {
        this.task = task;
        this.completed = completed;
    }

    // Empty constructor for Room
    public ToDo(String title, String description) {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTask() { return task; }
    public void setTask(String task) { this.task = task; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
