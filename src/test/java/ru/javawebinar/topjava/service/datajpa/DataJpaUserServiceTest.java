package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.NOT_FOUND;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(profiles = Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {
    @Test
    public void getWithMeals() throws Exception {
        User actualUser = service.getWithMeals(ADMIN_ID);
        USER_MATCHER.assertMatch(actualUser, ADMIN);

        List<Meal> expected = List.of(ADMIN_MEAL2, ADMIN_MEAL1);
        MEAL_MATCHER.assertMatch(actualUser.getMeals(), expected);
    }

    @Test
    public void getWithMealsNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.getWithMeals(NOT_FOUND));
    }

    @Test
    public void getWithMealsNoMeals() throws Exception {
        User actualUser = service.getWithMeals(USER_NO_MEALS_ID);
        USER_MATCHER.assertMatch(actualUser, USER_NO_MEALS);

        MEAL_MATCHER.assertMatch(actualUser.getMeals(), List.of());
    }
}
