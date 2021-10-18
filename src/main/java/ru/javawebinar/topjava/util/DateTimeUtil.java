package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenDatesAndTime(LocalDateTime ldt
            , LocalDate beginDate, LocalDate endDate
            , LocalTime startTime, LocalTime endTime) {
        return compareDates(ldt.toLocalDate(), beginDate, endDate) && compareTime(ldt.toLocalTime(), startTime, endTime);
    }

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    private static boolean compareDates(LocalDate ld, LocalDate beginDate, LocalDate endDate) {
        return ld.compareTo(beginDate) >= 0 && ld.compareTo(endDate) <= 0;
    }

    private static boolean compareTime(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) <= 0;
    }
}

