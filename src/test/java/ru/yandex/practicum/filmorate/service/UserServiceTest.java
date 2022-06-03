package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class UserServiceTest {

    private UserService userService;

    private User user;

    @BeforeEach
    void reload() {
        userService = new UserService(new InMemoryUserStorage());
        user = new User(1L, "user@mail.ru", "userLogin", null, LocalDate.of(1990, 01, 01));
    }

    @Test
    void addAndGetUser() {
        NoSuchUserException exception = assertThrows(NoSuchUserException.class, () ->
                userService.getUser(user.getId())
        );
        assertEquals("There is no such user", exception.getMessage());

        userService.addUser(user);
        assertEquals(user, userService.getUser(1L));
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

        assertArrayEquals(excpected.toArray(), actual.toArray());
    }

    @Test
    void updateUser() {
        User updatedUser = new User(1, "updatedUser@mail.ru", "updatedUserLogin", null, LocalDate.of(1990, 01, 01));
        NoSuchUserException exception = assertThrows(NoSuchUserException.class, () ->
                userService.updateUser(updatedUser)
        );
        assertEquals("There is no such user", exception.getMessage());

        userService.addUser(user);
        userService.updateUser(updatedUser);
        assertEquals(updatedUser, userService.getUser(user.getId()));
    }

    @Test
    void deleteUser() {
        userService.addUser(user);
        userService.deleteUser(user.getId());
        NoSuchUserException exception = assertThrows(NoSuchUserException.class, () ->
                userService.getUser(user.getId())
        );
        assertEquals("There is no such user", exception.getMessage());
    }

    @Test
    void deleteAll() {
        User anotherUser = new User(2, "anotherUser@mail.ru", "anotherUserLogin", null, LocalDate.of(1990, 01, 01));
        userService.addUser(user);
        userService.addUser(anotherUser);
        userService.deleteAll();
        assertEquals("[]", userService.getAll().toString());
    }

    @Test
    void addAndGetFriend() {
        User anotherUser = new User(2, "anotherUser@mail.ru", "anotherUserLogin", null, LocalDate.of(1990, 01, 01));
        userService.addUser(user);
        userService.addUser(anotherUser);
        userService.addFriend(user.getId(), anotherUser.getId());
        assertEquals(List.of(anotherUser), userService.getFriends(user.getId()));
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
        assertEquals(List.of(anotherUser), userService.getFriends(user.getId()));
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
        assertEquals(List.of(anotherOneUser), userService.getCommonFriends(user.getId(), anotherUser.getId()));
    }
}