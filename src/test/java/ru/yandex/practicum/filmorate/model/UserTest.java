package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UserTest {

    private User user;

    @BeforeEach
    public void reloadUser() {
        user = new User(1, "user@mail.ru", "userLogin", null,  LocalDate.of(1990, 01, 01));
    }

    @Test
    void getId() {
        Assertions.assertEquals(1, user.getId());
    }

    @Test
    void getEmail() {
        Assertions.assertEquals("user@mail.ru", user.getEmail());
    }

    @Test
    void getLogin() {
        Assertions.assertEquals("login", user.getLogin());
    }

    @Test
    void getName() {
        Assertions.assertEquals(null, user.getLogin());
    }

    @Test
    void getBirthday() {
        Assertions.assertEquals(LocalDate.of(1990, 01, 01), user.getBirthday());
    }

    @Test
    void setEmail() {
        String newEmail = "new@mail.ru";
        user.setEmail(newEmail);
        Assertions.assertEquals(newEmail, user.getEmail());
    }

    @Test
    void setLogin() {
        String newLogin = "new_login";
        user.setLogin(newLogin);
        Assertions.assertEquals(newLogin, user.getLogin());
    }

    @Test
    void setName() {
        String newName = "newName";
        user.setName(newName);
        Assertions.assertEquals(newName, user.getName());
    }

    @Test
    void setBirthday() {
        LocalDate newBirthday = LocalDate.of(1989, 01, 01);
        user.setBirthday(newBirthday);
        Assertions.assertEquals(newBirthday, user.getBirthday());
    }

    @Test
    void testEquals() {
        User sameUser = new User(1, "user@mail.ru", "userLogin", null,  LocalDate.of(1990, 01, 01));
        assertEquals(user, sameUser);

        sameUser.setName("sameUser");
        assertNotEquals(user, sameUser);
    }

    @Test
    void canEqual() {
        User sameUser = new User(1, "user@mail.ru", "sameLogin", "sameUser",  LocalDate.of(1990, 01, 01));
        Assertions.assertFalse(user.canEqual(sameUser));
    }

    @Test
    void testHashCode() {
        User sameUser = new User(1, "user@mail.ru", "userLogin", null,  LocalDate.of(1990, 01, 01));
        Assertions.assertEquals(user.hashCode(), sameUser.hashCode());

        sameUser.setName("sameUser");
        Assertions.assertNotEquals(user.hashCode(), sameUser.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("User(id=1, email=user@mail.ru, login=userLogin, name=null, birthday=1990-01-01)", user.toString());
    }
}