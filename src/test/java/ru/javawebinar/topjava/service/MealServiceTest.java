package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.MealTestData.assertMatch;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService service;

    @Autowired
    private MealRepository repository;

    @Test
    public void get() {
        Meal meal = service.get(MEAL_ID_USER_30_10, USER_ID);
        assertMatch(meal, MEAL_USER_30_10);
    }

    @Test
    public void getAnotherUserMeal() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID_ADMIN_01_14, USER_ID));
    }


    @Test
    public void delete() {
        service.delete(MEAL_ID_USER_30_10, USER_ID);
        assertNull(repository.get(MEAL_ID_USER_30_10, USER_ID));
    }

    @Test
    public void deleteAnotherUserMeal() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID_ADMIN_01_14, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> betweenInclusive = service.getBetweenInclusive(LocalDate.of(2020, 1, 31), LocalDate.of(2020, 1, 31), USER_ID);
        assertMatch(betweenInclusive, MEAL_USER_31_20, MEAL_USER_31_13, MEAL_USER_31_10, MEAL_USER_31_00);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(ADMIN_ID);
        assertMatch(all, MEAL_ADMIN_01_21, MEAL_ADMIN_01_14);
    }

    @Test
    public void update() {
        Meal updated = MealTestData.getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL_ID_USER_30_10, USER_ID), updated);
    }

    @Test
    public void updateAnotherUserMeal() {
        Meal updated = MealTestData.getUpdated();
        assertThrows(NotFoundException.class, () -> service.update(updated, ADMIN_ID));
    }

    @Test
    public void create() {
        Meal newMeal = MealTestData.getNew();
        Meal created = service.create(newMeal, USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void createDuplicateDateTimeForUser() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(null, LocalDateTime.of(2020, 1, 30, 10, 0), "Ещё один завтрак", 700), USER_ID));
    }

}