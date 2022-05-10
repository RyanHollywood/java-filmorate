package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmorateApplicationTests {

    private User user;
    private Film film;

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @BeforeEach
    public void reloadModels() {
        user = new User(1, "user@mail.ru", "userLogin", null, LocalDate.of(1990, 01, 01));
        film = new Film(1, "Film", "Film description", LocalDate.now().minusDays(1), Duration.ofHours(1));
    }

    @Test
    public void createValidUser() throws Exception {
        postWithOkRequest(user, "/users");
    }

    @Test
    public void createEmptyEmailUser() throws Exception {
        user.setEmail(null);
        postWithBadRequest(user, "/users");
    }

    @Test
    public void createBlankEmailUser() throws Exception {
        user.setEmail(" ");
        postWithBadRequest(user, "/users");
    }

    @Test
    public void createInvalidEmailUser() throws Exception {
        user.setEmail("userMail.ru");
        postWithBadRequest(user, "/users");
    }

    @Test
    public void createEmptyLoginUser() throws Exception {
        user.setLogin(null);
        postWithBadRequest(user, "/users");
    }

    @Test
    public void createBlankLoginUser() throws Exception {
        user.setLogin(" ");
        postWithBadRequest(user, "/users");
    }

    @Test
    public void createInvalidLoginUser() throws Exception {
        user.setLogin("user login");
        postWithBadRequest(user, "/users");
    }

    @Test
    public void createInvalidBirthdayUser() throws Exception {
        user.setBirthday(LocalDate.now().plusDays(1));
        postWithBadRequest(user, "/users");
    }

    @Test
    public void createValidFilm() throws Exception {
        postWithOkRequest(film, "/films");
    }

    @Test
    public void createEmptyNameFilm() throws Exception {
        film.setName(null);
        postWithBadRequest(film, "/films");
    }

    @Test
    public void createBlankNameFilm() throws Exception {
        film.setName(" ");
        postWithBadRequest(film, "/films");
    }

    @Test
    public void createEmptyDescriptionFilm() throws Exception {
        film.setDescription(null);
        postWithBadRequest(film, "/films");
    }

    @Test
    public void createBlankDescriptionFilm() throws Exception {
        film.setDescription(" ");
        postWithBadRequest(film, "/films");
    }

    @Test
    public void createSizeInvalidDescriptionFilm() throws Exception {
        String invalidDescription = "";
        for (int i = 0; i < 201; i++) {
            invalidDescription += "x";
        }
        film.setDescription(invalidDescription);
        postWithBadRequest(film, "/films");
    }

    @Test
    public void createInvalidReleaseDateFilm() throws Exception {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        postWithBadRequest(film, "/films");
    }


    @Test
    public void createInvalidDurationFilm() throws Exception {
        film.setDuration(Duration.ofHours(-1));
        postWithBadRequest(film, "/films");
    }

    private <T> void postWithOkRequest(T object, String path) throws Exception {
        mvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(object)))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(object)))
                .andReturn();
    }

    private <T> void postWithBadRequest(T object, String path) throws Exception {
        mvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(object)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

}
