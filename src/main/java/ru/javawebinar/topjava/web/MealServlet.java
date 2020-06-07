package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.MealRepositoryIntern;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final MealRepository mealRepository = MealRepositoryIntern.getInstance();

    public static final String JSP_MEAL_LIST = "/meals.jsp";
    public static final String JSP_MEAL = "/meal.jsp";
    public static final String REDIRECT_MEAL_LIST = "meals";

    public static final int CALORIES_PER_DAY_FOR_EXCESS = 2000;

    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        action = action == null ? "" : action.toLowerCase();
        log.debug("Get: action={}", action);

        switch (action) {
            case "delete":
                long id = Long.parseLong(request.getParameter("id"));
                mealRepository.delete(id);

                log.debug("Redirect to meal list");
                response.sendRedirect(REDIRECT_MEAL_LIST);
                break;
            case "edit":
                id = Long.parseLong(request.getParameter("id"));
                Meal meal = mealRepository.getById(id);

                log.debug("Forward to editing meal with id={}", id);
                request.setAttribute("meal", meal);
                getServletContext().getRequestDispatcher(JSP_MEAL).forward(request, response);
                break;
            case "insert":
                log.debug("Forward to adding new meal");
                getServletContext().getRequestDispatcher(JSP_MEAL).forward(request, response);
                break;
            default:
                List<Meal> meals = mealRepository.getAll();

                List<MealTo> mealsTo = MealsUtil.filteredByStreams(meals, LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY_FOR_EXCESS);
                request.setAttribute("mealsTo", mealsTo);

                log.debug("Forward to meal list");
                getServletContext().getRequestDispatcher(JSP_MEAL_LIST).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String idString = request.getParameter("id");
        String description = request.getParameter("description");
        String caloriesString = request.getParameter("calories");
        String dateTimeString = request.getParameter("dateTime");

        log.debug("Post: id={}, description={}, calories={}, dateTime={}", idString, description, caloriesString, dateTimeString);

        long id = (idString != null && !idString.isEmpty()) ? Long.parseLong(idString) : 0L;
        int calories = Integer.parseInt(caloriesString);
        dateTimeString = dateTimeString.replace("T", " ");
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (DateTimeParseException e) {
            log.error("Incorrect date format while meal post: {}", dateTimeString);
            e.printStackTrace();
            return;
        }

        Meal meal = new Meal(id, dateTime, description, calories);
        mealRepository.save(meal);

        log.debug("Redirect to meal list");
        response.sendRedirect(REDIRECT_MEAL_LIST);
    }

}
