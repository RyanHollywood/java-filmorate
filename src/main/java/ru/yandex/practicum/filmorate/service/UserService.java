package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserService {

    private long idCounter = 1;

    private InMemoryUserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getUser(Long id) {
        if (!userStorage.contains(id)) {
            throw new NoSuchUserException("There is no such user");
        }
        return userStorage.get(id);
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public void addUser(User user) {
        user.setId(getNewId());
        userStorage.add(user.getId(), user);
    }

    public void updateUser(User user) {
        if (!userStorage.contains(user.getId())) {
            throw new NoSuchUserException("There is no such user");
        }
        userStorage.update(user.getId(), user);
    }

    public void deleteUser(Long id) {
        if (!userStorage.contains(id)) {
            throw new NoSuchUserException("There is no such user");
        }
        userStorage.delete(id);
    }

    public void deleteAll() {
        idCounter = 1;
        userStorage.deleteAll();
    }

    public void addFriend(long id, long friendId) {
        if (!userStorage.contains(id) || !userStorage.contains(friendId)) {
            throw new NoSuchUserException("There is no such user");
        }
        userStorage.get(id).addFriend(friendId);
        userStorage.get(friendId).addFriend(id);
    }

    public void deleteFriend(long id, long friendId) {
        if (!userStorage.contains(id) || !userStorage.contains(friendId)) {
            throw new NoSuchUserException("There is no such user");
        }
        userStorage.get(id).deleteFriend(friendId);
    }

    public Collection<User> getFriends(long id) {
        Collection<User> friends = new ArrayList<>();
        for (long friend : userStorage.get(id).getFriends()) {
            friends.add(userStorage.get(friend));
        }
        return friends;
    }

    public Collection<User> getCommonFriends(long id, long friendId) {
        Collection<User> commonFriends = new ArrayList<>();
        for (long friend : userStorage.get(id).getFriends()) {
            if (userStorage.get(friendId).getFriends().contains(friend)) {
                commonFriends.add(userStorage.get(friend));
            }
        }
        return commonFriends;
    }

    private long getNewId() {
        return idCounter++;
    }
}
