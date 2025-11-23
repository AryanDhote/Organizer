package com.my.organizer.database;

import androidx.room.TypeConverter;

import com.my.organizer.models.ExpenseType;

import java.util.Date;

public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value) { return value == null ? null : new Date(value); }

    @TypeConverter
    public static Long dateToTimestamp(Date date) { return date == null ? null : date.getTime(); }

    @TypeConverter
    public static ExpenseType toExpenseType(String value) {
        if (value == null) return null;
        try { return ExpenseType.valueOf(value); } catch (IllegalArgumentException ex) { return null; }
    }

    @TypeConverter
    public static String fromExpenseType(ExpenseType type) { return type == null ? null : type.name(); }
}
