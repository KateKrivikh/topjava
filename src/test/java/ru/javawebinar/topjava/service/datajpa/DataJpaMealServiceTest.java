package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.TestMatcher;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.MealTestData.NOT_FOUND;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(profiles = Profiles.DATAJPA)
public class DataJpaMealServiceTest extends MealServiceTest {

    public static TestMatcher<Meal> MEAL_MATCHER_WITH_USER = TestMatcher.usingFieldsComparator();

    @Test
    public void getWithUser() throws Exception {
        Meal actual = service.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        Meal expected = ADMIN_MEAL1;
        expected.setUser(ADMIN);
        MEAL_MATCHER_WITH_USER.assertMatch(actual, expected);
    }

    @Test
    public void getWithUserNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.getWithUser(NOT_FOUND, USER_ID));
    }

    @Test
    public void getWithUserNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> service.getWithUser(MEAL1_ID, ADMIN_ID));
    }
}
