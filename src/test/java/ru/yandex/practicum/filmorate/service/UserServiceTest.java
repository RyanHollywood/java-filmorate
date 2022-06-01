package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.*;

class UserServiceTest {

    private UserService userService;

    private User user;

    @BeforeEach
    void reload() {
        userService = new UserService(new InMemoryUserStorage());
        user = new User(1L, "user@mail.ru", "userLogin", null, LocalDate.of(1990, 01, 01));
    }

    @Test
    void AddAndGetUser() {
        NoSuchUserException exception = Assertions.assertThrows(NoSuchUserException.class, () ->
                userService.getUser(user.getId())
        );
        Assertions.assertEquals("There is no such user", exception.getMessage());

        userService.addUser(user);
        Assertions.assertEquals(user, userService.getUser(1L));
    }

    @Test
    void getAll() {
        User anotherUser = new User(2, "anotherUser@mail.ru", "anotherUserLogin", null, LocalDate.of(1990, 01, 01));
        userService.addUser(user);
        userService.addUser(anotherUser);

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
        actual.addAll(userService.getAll());

        Assertions.assertArrayEquals(excpected.toArray(), actual.toArray());
    }

    @Test
    void updateUser() {
        User updatedUser = new User(1, "updatedUser@mail.ru", "updatedUserLogin", null, LocalDate.of(1990, 01, 01));
        NoSuchUserException exception = Assertions.assertThrows(NoSuchUserException.class, () ->
                userService.updateUser(updatedUser)
        );
        Assertions.assertEquals("There is no such user", exception.getMessage());

        userService.addUser(user);
        userService.updateUser(updatedUser);
        Assertions.assertEquals(updatedUser, userService.getUser(user.getId()));
    }

    @Test
    void deleteUser() {
        userService.addUser(user);
        userService.deleteUser(user.getId());
        NoSuchUserException exception = Assertions.assertThrows(NoSuchUserException.class, () ->
                userService.getUser(user.getId())
        );
        Assertions.assertEquals("There is no such user", exception.getMessage());
    }

    @Test
    void deleteAll() {
        User anotherUser = new User(2, "anotherUser@mail.ru", "anotherUserLogin", null, LocalDate.of(1990, 01, 01));
        userService.addUser(user);
        userService.addUser(anotherUser);
        userService.deleteAll();
        Assertions.assertEquals("[]", userService.getAll().toString());
    }

    @Test
    void addAndGetFriend() {
        User anotherUser = new User(2, "anotherUser@mail.ru", "anotherUserLogin", null, LocalDate.of(1990, 01, 01));
        userService.addUser(user);
        userService.addUser(anotherUser);
        userService.addFriend(user.getId(), anotherUser.getId());
        Assertions.assertEquals(List.of(anotherUser), userService.getFriends(user.getId()));
    }

    @Test
    void deleteFriend() {
        User anotherUser = new User(2, "anotherUser@mail.ru", "anotherUserLogin", null, LocalDate.of(1990, 01, 01));
        User anotherOneUser = new User(3, "anotherOneUser@mail.ru", "anotherOneUserLogin", null, LocalDate.of(1990, 01, 01));
        userService.addUser(user);
        userService.addUser(anotherUser);
        userService.addUser(anotherOneUser);
        userService.addFriend(user.getId(), anotherUser.getId());
        userService.addFriend(user.getId(), anotherOneUser.getId());
        userService.deleteFriend(user.getId(), anotherOneUser.getId());
        Assertions.assertEquals(List.of(anotherUser), userService.getFriends(user.getId()));
    }

    @Test
    void getCommonFriends() {
        User anotherUser = new User(2, "anotherUser@mail.ru", "anotherUserLogin", null, LocalDate.of(1990, 01, 01));
        User anotherOneUser = new User(3, "anotherOneUser@mail.ru", "anotherOneUserLogin", null, LocalDate.of(1990, 01, 01));
        userService.addUser(user);
        userService.addUser(anotherUser);
        userService.addUser(anotherOneUser);
        userService.addFriend(user.getId(), anotherUser.getId());
        userService.addFriend(user.getId(), anotherOneUser.getId());
        userService.addFriend(anotherUser.getId(), anotherOneUser.getId());
        Assertions.assertEquals(List.of(anotherOneUser), userService.getCommonFriends(user.getId(), anotherUser.getId()));
    }
}