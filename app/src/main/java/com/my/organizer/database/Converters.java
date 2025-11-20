package com.my.organizer.database;

import androidx.room.TypeConverter;

import com.my.organizer.models.ExpenseType;

import java.util.Date;

public class Converters {

    // --- Date converters (unchanged) ---
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    // --- ExpenseType converters ---
    @TypeConverter
    public static ExpenseType toExpenseType(String value) {
        return value == null ? null : ExpenseType.valueOf(value);
    }

    @TypeConverter
    public static String fromExpenseType(ExpenseType type) {
        return type == null ? null : type.name();
    }
}
