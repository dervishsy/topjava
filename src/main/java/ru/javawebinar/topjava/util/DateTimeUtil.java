package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    enum HalfOpen {
        CLOSED,
        LEFT_HALF_OPEN,
        RIGHT_HALF_OPEN,
        OPEN
    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static <T extends Comparable<T>> boolean isBetween(T value, T start, T end, HalfOpen halfopen) {
        boolean result = true;
        if ((start != null) && ((halfopen == HalfOpen.CLOSED) || (halfopen == HalfOpen.RIGHT_HALF_OPEN))) {
            result = result && (value.compareTo(start) >= 0);
        }
        if ((end != null) && ((halfopen == HalfOpen.CLOSED) || (halfopen == HalfOpen.LEFT_HALF_OPEN))) {
            result = result && (value.compareTo(end) <= 0);
        }
        if ((start != null) && ((halfopen == HalfOpen.LEFT_HALF_OPEN) || (halfopen == HalfOpen.OPEN))) {
            result = result && (value.compareTo(start) > 0);
        }
        if ((end != null) && ((halfopen == HalfOpen.RIGHT_HALF_OPEN) || (halfopen == HalfOpen.OPEN))) {
            result = result && (value.compareTo(end) < 0);
        }
        return result;
    }

    public static boolean isBetweenDates(LocalDate ld, LocalDate start, LocalDate end) {
        boolean result = true;
        if (start != null) result = result && (ld.compareTo(start) >= 0);
        if (end != null) result = result && (ld.compareTo(end) <= 0);
        return result;
    }

    public static boolean isBetweenTime(LocalTime lt, LocalTime start, LocalTime end) {
        boolean result = true;
        if (start != null) result = result && (lt.compareTo(start) >= 0);
        if (end != null) result = result && (lt.compareTo(end) < 0);
        return result;
    }

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate parseDate(String date) {
        if (date == null) return null;
        if (date.isEmpty()) return null;
        return LocalDate.parse(date);
    }

    public static LocalTime parseTime(String time) {
        if (time == null) return null;
        if (time.isEmpty()) return null;
        return LocalTime.parse(time);
    }
}

