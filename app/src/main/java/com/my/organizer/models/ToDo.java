package com.my.organizer.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "todos")
public class ToDo {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private Date date;

    public ToDo(String title, String description, Date date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public void setId(int id) { this.id = id; }
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Date getDate() { return date; }
}
