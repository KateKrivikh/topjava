package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

        System.out.println(filteredByCollector(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDays = new HashMap<>();
        for (UserMeal meal : meals) {
            caloriesByDays.merge(getDate(meal), meal.getCalories(), Integer::sum);
        }

        List<UserMealWithExcess> listUserMealWithExcesses = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(getTime(meal), startTime, endTime)) {
                boolean excess = isExcess(caloriesByDays.get(getDate(meal)), caloriesPerDay);
                UserMealWithExcess userMealWithExcess = createUserMealWithExcess(meal, excess);
                listUserMealWithExcesses.add(userMealWithExcess);
            }
        }

        return listUserMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDays = meals.stream()
                .collect(Collectors.groupingBy(UserMealsUtil::getDate, Collectors.summingInt(UserMeal::getCalories)));

        return meals.stream()
                .filter(e -> TimeUtil.isBetweenHalfOpen(getTime(e), startTime, endTime))
                .map(e -> {
                    boolean excess = isExcess(caloriesByDays.get(getDate(e)), caloriesPerDay);
                    return createUserMealWithExcess(e, excess);
                })
                .collect(Collectors.toList());
    }

    // TODO I do not extract this collector in separate class because I don't think my solution is good.
    // TODO Once I had another task like this one, but could not release collector.
    // TODO Other words: this is my first Collector, I expect your comments, please.
    public static List<UserMealWithExcess> filteredByCollector(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Collector<UserMeal, Map<LocalDate, Map<UserMeal, Integer>>, List<UserMealWithExcess>> collector = Collector.of(
                TreeMap::new,
                (map, userMeal) -> {
                    LocalDate localDate = getDate(userMeal);

                    // meals for concrete day, value is the same for all keys
                    Map<UserMeal, Integer> mealsWithTotalDayCalories = map.getOrDefault(localDate, new HashMap<>());

                    // forming total calories for concrete day: current + all previous
                    int totalDayCalories = userMeal.getCalories();
                    Iterator<Integer> iterator = mealsWithTotalDayCalories.values().iterator();
                    // only first, all values are the same
                    if (iterator.hasNext())
                        totalDayCalories += iterator.next();

                    // refresh all previous meals
                    for (Map.Entry<UserMeal, Integer> entry : mealsWithTotalDayCalories.entrySet()) {
                        entry.setValue(totalDayCalories);
                    }
                    // add current meal
                    mealsWithTotalDayCalories.put(userMeal, totalDayCalories);

                    map.put(localDate, mealsWithTotalDayCalories);
                },
                (l, r) -> {
                    l.putAll(r);
                    return l;
                },
                (m) -> {
                    List<UserMealWithExcess> resultList = new ArrayList<>();
                    for (Map<UserMeal, Integer> mealsWithTotalDayCalories : m.values()) {
                        // in this cycle count of entries will be 3-5, don't think than complexity is O(N*N): O(N*5) ~ O(N)
                        for (Map.Entry<UserMeal, Integer> currentMealWithTotalDayCalories : mealsWithTotalDayCalories.entrySet()) {
                            UserMeal meal = currentMealWithTotalDayCalories.getKey();
                            // TODO i don't like using the "startTime", "endTime" and "caloriesPerDay" directly here.
                            // TODO But i don't know how to do better
                            if (TimeUtil.isBetweenHalfOpen(getTime(meal), startTime, endTime)) {
                                boolean excess = isExcess(currentMealWithTotalDayCalories.getValue(), caloriesPerDay);
                                UserMealWithExcess userMealWithExcess = createUserMealWithExcess(meal, excess);
                                resultList.add(userMealWithExcess);
                            }
                        }
                    }
                    return resultList;
                });

        return meals.stream().collect(collector);
    }


    private static LocalDate getDate(UserMeal meal) {
        return meal.getDateTime().toLocalDate();
    }

    private static LocalTime getTime(UserMeal meal) {
        return meal.getDateTime().toLocalTime();
    }

    private static boolean isExcess(int caloriesPerCurrentDay, int limitedCaloriesPerDay) {
        return caloriesPerCurrentDay > limitedCaloriesPerDay;
    }

    private static UserMealWithExcess createUserMealWithExcess(UserMeal meal, boolean excess) {
        return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
