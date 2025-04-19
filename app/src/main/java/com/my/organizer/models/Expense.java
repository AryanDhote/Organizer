package com.my.organizer.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "expenses")
public class Expense {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String description;
    private double amount;
    private Date date;

    public Expense(String description, double amount, Date date) {
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public void setId(int id) { this.id = id; }
    public int getId() { return id; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public Date getDate() { return date; }
}
