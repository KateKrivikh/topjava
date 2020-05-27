package ru.javawebinar.topjava.model;

import ru.javawebinar.topjava.exception.MealForAnotherDayException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserMealAllDay {
    private final LocalDate date;
    private int calories;
    private final List<UserMeal> meals;

    public UserMealAllDay(LocalDate date) {
        this.date = date;
        calories = 0;
        meals = new ArrayList<>();
    }

    public void addMeal(UserMeal meal) {
        if (!meal.getDateTime().toLocalDate().equals(date))
            throw new MealForAnotherDayException();

        meals.add(meal);
        calories += meal.getCalories();
    }

    public LocalDate getDate() {
        return date;
    }

    public int getCalories() {
        return calories;
    }

    public List<UserMeal> getMeals() {
        return meals;
    }
}
