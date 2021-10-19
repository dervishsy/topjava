package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    enum HalfOpen {
        NO_HALFOPEN,
        LEFT_HALFOPEN,
        RIGHT_HALFOPEN,
        BOTH_HALFOPEN
    }
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenDatesAndTime(LocalDateTime ldt
            , LocalDate beginDate, LocalDate endDate
            , LocalTime startTime, LocalTime endTime) {
        return isBetween(ldt.toLocalDate(), beginDate, endDate,HalfOpen.NO_HALFOPEN)
                && isBetween(ldt.toLocalTime(), startTime, endTime,HalfOpen.RIGHT_HALFOPEN);
    }

    private static <T extends Comparable<? super T>>boolean isBetween(T value, T start, T end, HalfOpen halfopen) {
        boolean result = true;
        if ((start != null)&&((halfopen==HalfOpen.NO_HALFOPEN)||(halfopen==HalfOpen.RIGHT_HALFOPEN))) {
            result = result && (value.compareTo(start) >= 0);
        }
        if ((end != null)&&((halfopen==HalfOpen.NO_HALFOPEN)||(halfopen==HalfOpen.LEFT_HALFOPEN))) {
            result = result && (value.compareTo(end) <= 0);
        }
        if ((start != null)&&((halfopen==HalfOpen.LEFT_HALFOPEN)||(halfopen==HalfOpen.BOTH_HALFOPEN))) {
            result = result && (value.compareTo(start) > 0);
        }
        if ((end != null)&&((halfopen==HalfOpen.RIGHT_HALFOPEN)||(halfopen==HalfOpen.BOTH_HALFOPEN))) {
            result = result && (value.compareTo(end) < 0);
        }
        return result;
    }

    private static boolean isBetweenDates(LocalDate ld, LocalDate start, LocalDate end) {
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

    public static LocalDate parseDate(String dateOrTime) {
        if (dateOrTime.isEmpty()) return null;
        return LocalDate.parse(dateOrTime);
    }

    public static LocalTime parseTime(String dateOrTime) {
        if (dateOrTime.isEmpty()) return null;
        return LocalTime.parse(dateOrTime);
    }
}

