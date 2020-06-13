package ru.javawebinar.topjava.web;

import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {
    private static int userId = 1;

    private static final Logger log = getLogger(SecurityUtil.class);

    public static int authUserId() {
        return userId;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }

    public static void setAuthUserId(int userId) {
        log.info("Set auth id={}", userId);
        SecurityUtil.userId = userId;
    }
}