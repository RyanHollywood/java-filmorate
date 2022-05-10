package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class FilmorateApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void createValidUser() throws Exception {

        User user = new User(1, "user@mail.ru", "userLogin", LocalDate.of(1990, 01, 01));

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(user)))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(user)))
                .andReturn();
    }

    @Test
    public void createNonValidEmailUser() throws Exception {

        User user = new User(1, "userMail.ru", "userLogin", LocalDate.of(1990, 01, 01));

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(user)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void createNonValidLoginUser() throws Exception {

        User user = new User(1, "user@mail.ru", "user login", LocalDate.of(1990, 01, 01));

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(user)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void createNonValidBirthdayUser() throws Exception {

        User user = new User(1, "userMail.ru", "userLogin", LocalDate.now().plusDays(1));

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(user)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

}
