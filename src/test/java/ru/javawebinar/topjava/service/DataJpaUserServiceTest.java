package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(profiles = "datajpa")
public class DataJpaUserServiceTest extends UserServiceTest {
    @Test
    public void getWithMeals() throws Exception {
        User actualUser = service.getWithMeals(ADMIN_ID);
        USER_MATCHER.assertMatch(actualUser, ADMIN);

        List<Meal> actual = actualUser.getMeals().stream()
                .sorted(Comparator.comparing(Meal::getDateTime, Comparator.reverseOrder()))
                .collect(Collectors.toList());
        List<Meal> expected = List.of(ADMIN_MEAL2, ADMIN_MEAL1);
        MEAL_MATCHER.assertMatch(actual, expected);
    }

}
