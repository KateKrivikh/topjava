package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealRepository {

    List<Meal> getAll();

    Meal getById(long id);

    void save(Meal meal);

    void delete(long id);
}
