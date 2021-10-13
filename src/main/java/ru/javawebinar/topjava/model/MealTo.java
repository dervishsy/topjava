package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;

public class MealTo {
    private final int id;

    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final boolean excess;

    public MealTo(Integer id, LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
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

    public boolean isExcess() {
        return this.excess;
    }

    public int getId() {
        return this.id;
    }

    public String toString() {
        return "MealTo{dateTime=" + this.dateTime
                + ", description='" + this.description
                + '\''
                + ", calories=" + this.calories
                + ", excess=" + this.excess + '}';
    }
}
