package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
public class JspMealController extends AbstractMealController {
    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping("/meals")
    public String getMeals(Model model) {
        model.addAttribute("meals", getAll());
        return "meals";
    }

    @GetMapping("/meals/filter")
    public String getMealsBetween(HttpServletRequest request, Model model) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));

        model.addAttribute("meals", getAllBetween(startDate, endDate, startTime, endTime));
        return "meals";
    }

    @GetMapping("/meals/create")
    public String getMealNew(HttpServletRequest request) {
        request.setAttribute("meal", getNew());
        return "mealForm";
    }

    @GetMapping("/meals/update")
    public String getMeal(HttpServletRequest request) {
        int id = getId(request);
        request.setAttribute("meal", get(id));
        return "mealForm";
    }

    @GetMapping("/meals/delete")
    public String deleteMeal(HttpServletRequest request) {
        int id = getId(request);
        delete(id);
        return "redirect:/meals";
    }

    @PostMapping("/meals")
    public String saveMeal(HttpServletRequest request) {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (isNew(request)) {
            create(meal);
        } else {
            int id = getId(request);
            update(meal, id);
        }

        return "redirect:meals";
    }
}
