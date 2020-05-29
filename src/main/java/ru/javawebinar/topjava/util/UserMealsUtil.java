package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealAllDay;
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
                .filter(m -> TimeUtil.isBetweenHalfOpen(getTime(m), startTime, endTime))
                .map(m -> {
                    boolean excess = isExcess(caloriesByDays.get(getDate(m)), caloriesPerDay);
                    return createUserMealWithExcess(m, excess);
                })
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByCollector(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Such spagetti-code is relevant? How can I refactor this better?
        Collector<UserMeal, Map<LocalDate, UserMealAllDay>, List<UserMealWithExcess>> collector = Collector.of(
                HashMap::new,
                (map, userMeal) -> {
                    LocalDate localDate = getDate(userMeal);

                    UserMealAllDay userMealAllDay = map.getOrDefault(localDate, new UserMealAllDay(localDate));
                    userMealAllDay.addMeal(userMeal);

                    map.put(localDate, userMealAllDay);
                },
                (mapMealResult, mapMealCombining) -> {
                    mapMealCombining.forEach((date, mealAllDay) -> {
                        if (mapMealResult.containsKey(date)) {
                            UserMealAllDay userMealAllDay = mapMealResult.get(date);
                            for (UserMeal meal : mealAllDay.getMeals()) {
                                userMealAllDay.addMeal(meal);
                            }
                        } else
                            mapMealResult.put(date, mealAllDay);
                    });
                    return mapMealResult;
                },
                (map) -> {
                    List<UserMealWithExcess> resultList = new ArrayList<>();
                    for (UserMealAllDay userMealAllDay : map.values()) {
                        boolean excess = isExcess(userMealAllDay.getCalories(), caloriesPerDay);
                        for (UserMeal meal : userMealAllDay.getMeals()) {
                            if (TimeUtil.isBetweenHalfOpen(getTime(meal), startTime, endTime)) {
                                UserMealWithExcess userMealWithExcess = createUserMealWithExcess(meal, excess);
                                resultList.add(userMealWithExcess);
                            }
                        }
                    }
                    return resultList;
                });


        return meals.parallelStream().collect(collector);
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
