package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

class InMemoryUserStorageTest {

    private InMemoryUserStorage userStorage;
    private User user;

    @BeforeEach
    void reload() {
        userStorage = new InMemoryUserStorage();
        user = new User(1, "user@mail.ru", "userLogin", null, LocalDate.of(1990, 01, 01));
    }

    @Test
    void addAndGet() {
        userStorage.add(user);
        Assertions.assertEquals(user, userStorage.get(user.getId()));
    }

    @Test
    void getAll() {
        User anotherUser = new User(2, "anotherUser@mail.ru", "anotherUserLogin", null, LocalDate.of(1990, 01, 01));
        userStorage.add(user);
        userStorage.add(anotherUser);

        Collection<User> excpected = new TreeSet<>((user1, user2) -> {
            if (user1.getId() < user2.getId()) {
                return 1;
            } else {
                return -1;
            }
        });
        excpected.add(user);
        excpected.add(anotherUser);

        Collection<User> actual = new TreeSet<>((user1, user2) -> {
            if (user1.getId() < user2.getId()) {
                return 1;
            } else {
                return -1;
            }
        });
        actual.addAll(userStorage.getAll());

        Assertions.assertArrayEquals(excpected.toArray(), actual.toArray());
    }

    @Test
    void update() {
        User updatedUser = new User(1, "updatedUser@mail.ru", "updatedUserLogin", null, LocalDate.of(1990, 01, 01));
        userStorage.add(user);
        userStorage.update(updatedUser);
        Assertions.assertEquals(updatedUser, userStorage.get(user.getId()));
    }

    @Test
    void delete() {
        userStorage.add(user);
        userStorage.delete(user.getId());
        Assertions.assertNull(userStorage.get(user.getId()));
    }

    @Test
    void deleteAll() {
        User anotherUser = new User(2, "anotherUser@mail.ru", "anotherUserLogin", null, LocalDate.of(1990, 01, 01));
        userStorage.add(user);
        userStorage.add(anotherUser);
        userStorage.deleteAll();
        Assertions.assertEquals("[]", userStorage.getAll().toString());
    }

    @Test
    void contains() {
        userStorage.add(user);
        Assertions.assertTrue(userStorage.contains(user.getId()));
    }
}