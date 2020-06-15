package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(m -> save(m, 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {}", meal);

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        } else if (repository.getOrDefault(userId, new ConcurrentHashMap<>()).get(meal.getId()) == null) {
            return null;
        }

        repository.putIfAbsent(userId, new ConcurrentHashMap<>());
        repository.get(userId).put(meal.getId(), meal);
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {}", id);
        return repository.getOrDefault(userId, new ConcurrentHashMap<>()).remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {}", id);
        return repository.getOrDefault(userId, new ConcurrentHashMap<>()).get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll");
        return getMeals(userId, m -> true);
    }

    @Override
    public List<Meal> getAllFilterByDate(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getAll filtered from {} to {}", startDate, endDate);
        Predicate<Meal> mealPredicate = m -> DateTimeUtil.isBetweenHalfOpen(m.getDateTime(), startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));
        return getMeals(userId, mealPredicate);
    }


    private List<Meal> getMeals(int userId, Predicate<Meal> filter) {
        return repository.getOrDefault(userId, new ConcurrentHashMap<>()).values().stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

