package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Meal {
    private final Integer id;

    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    public Meal() {
        this.id = null;
        this.dateTime = LocalDateTime.now();
        this.description = "";
        this.calories = 0;
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public Integer getId() {
        return this.id;
    }

    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    public String getDescription() {
        return this.description;
    }

    public int getCalories() {
        return this.calories;
    }

    public LocalDate getDate() {
        return this.dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return this.dateTime.toLocalTime();
    }

    public String toString() {
        return "Meal{id=" + this.id + ", dateTime=" + this.dateTime + ", description='" + this.description + '\'' + ", calories=" + this.calories + '}';
    }
}
