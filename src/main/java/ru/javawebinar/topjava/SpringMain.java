package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));
            System.out.println();

            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            System.out.println("All meals sorted by date desc:");
            List<MealTo> all = mealRestController.getAll(null, null, null, null);
            all.forEach(System.out::println);
            System.out.println();

            System.out.println("Filtered meals:");
            List<MealTo> allFiltered = mealRestController.getAll(null, LocalTime.of(13, 0), LocalDate.of(2020, 1, 31), LocalTime.of(20, 0));
            allFiltered.forEach(System.out::println);
            System.out.println();
        }
    }
}
