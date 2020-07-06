package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.TestMatcher;
import ru.javawebinar.topjava.model.Meal;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;

@ActiveProfiles(profiles = "datajpa")
public class DataJpaMealServiceTest extends MealServiceTest {

    public static TestMatcher<Meal> MEAL_MATCHER_WITH_USER = TestMatcher.usingFieldsComparator();

    @Test
    public void getWithUser() throws Exception {
        Meal actual = service.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        Meal expected = ADMIN_MEAL1;
        expected.setUser(ADMIN);
        MEAL_MATCHER_WITH_USER.assertMatch(actual, expected);
    }
}
