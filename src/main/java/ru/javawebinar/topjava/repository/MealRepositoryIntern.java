package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.slf4j.LoggerFactory.getLogger;

public class MealRepositoryIntern implements MealRepository {

    private final Map<Long, Meal> meals = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong(0);

    private static final Logger log = getLogger(MealRepositoryIntern.class);

    public MealRepositoryIntern() {
        initTestMeals();
    }

    @Override
    public List<Meal> getAll() {
        log.debug("Get all");
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal getById(long id) {
        log.debug("Get by id={}", id);
        return meals.get(id);
    }

    @Override
    public Meal save(Meal meal) {
        Meal returnedMeal = null;

        long id = meal.getId();
        if (id == 0) {
            id = counter.incrementAndGet();
            meal = new Meal(id, meal.getDateTime(), meal.getDescription(), meal.getCalories());
            if (meals.putIfAbsent(id, meal) == null)
                returnedMeal = meal;
        } else {
            returnedMeal = meals.replace(id, meal);
        }

        if (returnedMeal == null)
            log.warn("Save failed: id={}", id);
        else
            log.debug("Save: {}", returnedMeal);
        return returnedMeal;
    }

    @Override
    public void delete(long id) {
        log.debug("Remove by id={}", id);
        meals.remove(id);
    }

    public void initTestMeals() {
        save(new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        save(new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        save(new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        save(new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        save(new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        save(new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        save(new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

}
