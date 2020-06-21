package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_ID_USER_30_10 = START_SEQ + 2;
    public static final int MEAL_ID_USER_30_13 = START_SEQ + 3;
    public static final int MEAL_ID_USER_30_20 = START_SEQ + 4;
    public static final int MEAL_ID_USER_31_00 = START_SEQ + 5;
    public static final int MEAL_ID_USER_31_10 = START_SEQ + 6;
    public static final int MEAL_ID_USER_31_13 = START_SEQ + 7;
    public static final int MEAL_ID_USER_31_20 = START_SEQ + 8;

    public static final int MEAL_ID_ADMIN_01_14 = START_SEQ + 9;
    public static final int MEAL_ID_ADMIN_01_21 = START_SEQ + 10;

    public static final Meal MEAL_USER_30_10 = new Meal(MEAL_ID_USER_30_10, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal MEAL_USER_30_13 = new Meal(MEAL_ID_USER_30_13, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal MEAL_USER_30_20 = new Meal(MEAL_ID_USER_30_20, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal MEAL_USER_31_00 = new Meal(MEAL_ID_USER_31_00, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
    public static final Meal MEAL_USER_31_10 = new Meal(MEAL_ID_USER_31_10, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal MEAL_USER_31_13 = new Meal(MEAL_ID_USER_31_13, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
    public static final Meal MEAL_USER_31_20 = new Meal(MEAL_ID_USER_31_20, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);

    public static final Meal MEAL_ADMIN_01_14 = new Meal(MEAL_ID_ADMIN_01_14, LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510);
    public static final Meal MEAL_ADMIN_01_21 = new Meal(MEAL_ID_ADMIN_01_21, LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 30), "Дополнение к завтраку", 100);
    }

    public static Meal getUpdated() {
        return new Meal(MEAL_ID_USER_30_10, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 30), "Завтрак с опозданием", 1100);
    }


    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingDefaultElementComparator().isEqualTo(expected);
    }
}
