package com.my.organizer.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import java.io.Serializable;  // Implement Serializable
import java.util.Date;

@Entity(tableName = "expense_table")
public class Expense implements Serializable {  // Implements Serializable

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "amount")
    private double amount;

    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "type")
    private ExpenseType type;

    @ColumnInfo(name = "note")
    private String note;

    public Expense(double amount, Date date, ExpenseType type, String note) {
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.note = note;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public ExpenseType getType() { return type; }
    public void setType(ExpenseType type) { this.type = type; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
