package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.ADMIN_MEAL_ID;
import static ru.javawebinar.topjava.UserTestData.*;

class AdminUIControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminUIController.REST_URL + '/';

    @Autowired
    private UserService userService;

    @Test
    void getAll() throws Exception {
        perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(ADMIN, USER));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + USER_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> userService.get(USER_ID));
    }

    @Test
    void create() throws Exception {
        User newUser = getNew();
        newUser.setEnabled(true);
        newUser.setCaloriesPerDay(2000);

        perform(post(REST_URL)
                .param("id", "")
                .param("name", newUser.getName())
                .param("email", newUser.getEmail())
                .param("password", newUser.getPassword())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        newUser.setId(ADMIN_MEAL_ID + 2);
        USER_MATCHER.assertMatch(userService.get(ADMIN_MEAL_ID + 2), newUser);
    }

    @Test
    void enable() throws Exception {
        boolean expected = false;
        perform(post(REST_URL + USER_ID)
                .param("enabled", String.valueOf(expected)))
                .andDo(print())
                .andExpect(status().isNoContent());
        User user = userService.get(USER_ID);
        assertThat(user.isEnabled()).isEqualTo(expected);
    }
}