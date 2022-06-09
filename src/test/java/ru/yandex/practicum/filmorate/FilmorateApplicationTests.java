package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmorateApplicationTests {

    private User user;
    private Film film;

    private final String USERS_PATH = "/users";
    private final String FILMS_PATH = "/films";

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @BeforeEach
    public void reloadModels() throws Exception {
        mvc.perform(delete(USERS_PATH));
        mvc.perform(delete(FILMS_PATH));
        user = new User(1, "user@mail.ru", "userLogin", null, LocalDate.of(1990, 01, 01));
        film = new Film(1, "Film", "Film description", LocalDate.now().minusDays(1), Duration.ofHours(1));
    }

    @Test
    public void createValidUser() throws Exception {
        postWithOkRequest(user, USERS_PATH);
    }

    @Test
    public void createEmptyEmailUser() throws Exception {
        user.setEmail(null);
        postWithBadRequest(user, USERS_PATH);
    }

    @Test
    public void createBlankEmailUser() throws Exception {
        user.setEmail(" ");
        postWithBadRequest(user, USERS_PATH);
    }

    @Test
    public void createInvalidEmailUser() throws Exception {
        user.setEmail("userMail.ru");
        postWithBadRequest(user, USERS_PATH);
    }

    @Test
    public void createEmptyLoginUser() throws Exception {
        user.setLogin(null);
        postWithBadRequest(user, USERS_PATH);
    }

    @Test
    public void createBlankLoginUser() throws Exception {
        user.setLogin(" ");
        postWithBadRequest(user, USERS_PATH);
    }

    @Test
    public void createInvalidLoginUser() throws Exception {
        user.setLogin("user login");
        postWithBadRequest(user, USERS_PATH);
    }

    @Test
    public void createInvalidBirthdayUser() throws Exception {
        user.setBirthday(LocalDate.now().plusDays(1));
        postWithBadRequest(user, USERS_PATH);
    }

    @Test
    public void getUserById() throws Exception {
        createValidUser();
        mvc.perform(get(USERS_PATH + "/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(user)))
                .andReturn();
    }

    @Test
    public void getUserByIdNotFound() throws Exception {
        getWithNotFound(USERS_PATH, user.getId());
    }

    @Test
    public void updateUserNotFound() throws Exception {
        putWithNotFound(user, USERS_PATH);
    }

    @Test
    public void deleteUserNotFound() throws Exception {
        deleteByIdWithNotFound(USERS_PATH, user.getId());
    }

    @Test
    public void deleteAllUsers() throws Exception {
        mvc.perform(delete(USERS_PATH));
    }

    @Test
    public void createValidFilm() throws Exception {
        postWithOkRequest(film, FILMS_PATH);
    }

    @Test
    public void createEmptyNameFilm() throws Exception {
        film.setName(null);
        postWithBadRequest(film, FILMS_PATH);
    }

    @Test
    public void createBlankNameFilm() throws Exception {
        film.setName(" ");
        postWithBadRequest(film, FILMS_PATH);
    }

    @Test
    public void createEmptyDescriptionFilm() throws Exception {
        film.setDescription(null);
        postWithBadRequest(film, FILMS_PATH);
    }

    @Test
    public void createBlankDescriptionFilm() throws Exception {
        film.setDescription(" ");
        postWithBadRequest(film, FILMS_PATH);
    }

    @Test
    public void createSizeInvalidDescriptionFilm() throws Exception {
        String invalidDescription = "";
        for (int i = 0; i < 201; i++) {
            invalidDescription += "x";
        }
        film.setDescription(invalidDescription);
        postWithBadRequest(film, FILMS_PATH);
    }

    @Test
    public void createInvalidReleaseDateFilm() throws Exception {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        postWithBadRequest(film, FILMS_PATH);
    }

    @Test
    public void createInvalidDurationFilm() throws Exception {
        film.setDuration(Duration.ofHours(-1));
        postWithBadRequest(film, FILMS_PATH);
    }

    @Test
    public void getUsers() throws Exception {
        postWithOkRequest(user, USERS_PATH);

        JSONArray usersArray = new JSONArray();
        usersArray.put(new JSONObject(mapper.writeValueAsString(user)));

        mvc.perform(get(USERS_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(String.valueOf(usersArray)))
                .andReturn();
    }

    @Test
    public void getFilms() throws Exception {
        postWithOkRequest(film, FILMS_PATH);

        JSONArray filmsArray = new JSONArray();
        filmsArray.put(new JSONObject(mapper.writeValueAsString(film)));

        mvc.perform(get(FILMS_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(String.valueOf(filmsArray)))
                .andReturn();
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

    private void getWithNotFound(String path, long id) throws Exception {
        mvc.perform(get(path + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    private <T> void putWithNotFound(T object, String path) throws Exception {
        mvc.perform(put(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(object)))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    private void deleteByIdWithNotFound(String path, long id) throws Exception {
        mvc.perform(delete(path + "/" + id))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
