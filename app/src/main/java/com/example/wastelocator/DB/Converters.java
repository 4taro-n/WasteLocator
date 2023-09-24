package com.example.wastelocator.DB;

import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Converters {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ISO_LOCAL_TIME;

    @TypeConverter
    public static LocalDate fromStringToDate(String value) {
        return value == null ? null : LocalDate.parse(value, DATE_FORMAT);
    }

    @TypeConverter
    public static String fromDateToString(LocalDate date) {
        return date == null ? null : date.format(DATE_FORMAT);
    }

    @TypeConverter
    public static LocalTime fromStringToTime(String value) {
        return value == null ? null : LocalTime.parse(value, TIME_FORMAT);
    }

    @TypeConverter
    public static String fromTimeToString(LocalTime time) {
        return time == null ? null : time.format(TIME_FORMAT);
    }
}
