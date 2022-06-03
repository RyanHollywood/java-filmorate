package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void reload() {
        user = new User(1,"user@mail.ru", "userLogin", null,  LocalDate.of(1990, 01, 01));
    }

    @Test
    void getEmail() {
        assertEquals("user@mail.ru", user.getEmail());
    }

    @Test
    void getLogin() {
        assertEquals("userLogin", user.getLogin());
    }

    @Test
    void getName() {
        assertEquals("userLogin", user.getName());
        user.setName("  ");
        assertEquals("userLogin", user.getName());
    }

    @Test
    void getBirthday() {
        assertEquals(LocalDate.of(1990, 01, 01), user.getBirthday());
    }

    @Test
    void setEmail() {
        String newEmail = "new@mail.ru";
        user.setEmail(newEmail);
        assertEquals(newEmail, user.getEmail());
    }

    @Test
    void setLogin() {
        String newLogin = "new_login";
        user.setLogin(newLogin);
        assertEquals(newLogin, user.getLogin());
    }

    @Test
    void setName() {
        String newName = "newName";
        user.setName(newName);
        assertEquals(newName, user.getName());
    }

    @Test
    void setBirthday() {
        LocalDate newBirthday = LocalDate.of(1989, 01, 01);
        user.setBirthday(newBirthday);
        assertEquals(newBirthday, user.getBirthday());
    }

    @Test
    void testEquals() {
        User sameUser = new User(1L,"user@mail.ru", "userLogin", null,  LocalDate.of(1990, 01, 01));
        assertEquals(user, sameUser);

        sameUser.setName("sameUser");
        assertNotEquals(user, sameUser);
    }

    @Test
    void canEqual() {
        User sameUser = new User(1L, "user@mail.ru", "sameLogin", "sameUser",  LocalDate.of(1990, 01, 01));
        assertTrue(user.canEqual(sameUser));
    }

    @Test
    void testHashCode() {
        User sameUser = new User(1L, "user@mail.ru", "userLogin", null,  LocalDate.of(1990, 01, 01));
        assertEquals(user.hashCode(), sameUser.hashCode());

        sameUser.setName("sameUser");
        assertNotEquals(user.hashCode(), sameUser.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("User(id=1, email=user@mail.ru, login=userLogin, name=userLogin, birthday=1990-01-01, friends=[])", user.toString());
    }
}