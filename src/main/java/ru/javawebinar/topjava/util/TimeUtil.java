package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeUtil {
    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }

    public static boolean isLocalDateTimeInLimits(LocalDateTime dateTime, LocalTime startTime, LocalTime endTime) {
        LocalTime localTime = dateTime.toLocalTime();
        return !(localTime.isBefore(startTime) || localTime.isAfter(endTime));
    }

    public static LocalDate getDate(LocalDateTime ldt) {
        return ldt.toLocalDate();
    }
}
