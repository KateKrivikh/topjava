package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.slf4j.LoggerFactory.getLogger;

public class MealRepositoryIntern implements MealRepository {
    private static final MealRepositoryIntern instance = new MealRepositoryIntern();

    private static final Map<Long, Meal> meals = new ConcurrentHashMap<>();
    private static final AtomicLong counter = new AtomicLong(0);

    private static final Logger log = getLogger(MealRepositoryIntern.class);

    static {
        // TODO Don't understand where should be done
        MealsUtil.initTestMeals();
    }

    private MealRepositoryIntern() {
    }

    public static MealRepositoryIntern getInstance() {
        return instance;
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
    public void save(Meal meal) {
        long id = meal.getId();
        if (id == 0) {
            id = counter.incrementAndGet();
            meal = new Meal(id, meal.getDateTime(), meal.getDescription(), meal.getCalories());
        }
        log.debug("Save: {}", meal);
        meals.put(meal.getId(), meal);
    }

    @Override
    public void delete(long id) {
        log.debug("Remove by id={}", id);
        meals.remove(id);
    }

}
