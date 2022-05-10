package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    public void reloadModels() {
        user = new User(1, "user@mail.ru", "userLogin", null,  LocalDate.of(1990, 01, 01));
    }

    @Test
    void getId() {
    }

    @Test
    void getEmail() {
    }

    @Test
    void getLogin() {
    }

    @Test
    void getName() {
    }

    @Test
    void getBirthday() {
    }

    @Test
    void setEmail() {
        user.setEmail(null);
    }

    @Test
    void setLogin() {
    }

    @Test
    void setName() {
    }

    @Test
    void setBirthday() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void canEqual() {
    }

    @Test
    void testHashCode() {
    }

    @Test
    void testToString() {
    }
}