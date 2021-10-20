package ru.javawebinar.topjava.web;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {
    public static final int ADMIN_ID = 1;

    public static final int USER_ID = 2;

    private static int userId = 1;

    public static void setUserId(int userId) {
        SecurityUtil.userId = userId;
    }

    public static int authUserId() {
        return userId;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}