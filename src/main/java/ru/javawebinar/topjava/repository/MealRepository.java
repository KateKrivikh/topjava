package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MealRepository {
    private static final Map<Long, Meal> meals = new ConcurrentHashMap<>();
    private static Long counter = 0L;

    static {
        List<Meal> list = MealsUtil.initMeals();
        list.forEach(m -> meals.put(++counter, m));
    }

    public static List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }

}
