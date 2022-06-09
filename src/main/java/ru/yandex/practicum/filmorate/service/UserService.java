package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
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
            log.warn("GET REQUEST UNSUCCESSFUL - NO USER WITH ID:" + id + " FOUND");
            throw new NoSuchUserException("There is no such user");
        }
        log.debug("GET REQUEST SUCCESSFUL - USER WITH ID:" + id + " FOUND");
        return userStorage.get(id);
    }

    public Collection<User> getAll() {
        log.debug("GET REQUEST SUCCESSFUL - GET ALL USERS");
        return userStorage.getAll();
    }

    public void addUser(User user) {
        user.setId(getNewId());
        userStorage.add(user);
        log.debug("POST REQUEST SUCCESSFUL - USER WITH ID:" + user.getId() + " CREATED");
    }

    public void updateUser(User user) {
        if (!userStorage.contains(user.getId())) {
            log.warn("PUT REQUEST UNSUCCESSFUL - NO USER WITH ID:" + user.getId() + " FOUND");
            throw new NoSuchUserException("There is no such user");
        }
        userStorage.update(user);
        log.debug("PUT REQUEST SUCCESSFUL - USER WITH ID:" + user.getId() + " UPDATED");
    }

    public void deleteUser(long id) {
        if (!userStorage.contains(id)) {
            log.warn("DELETE REQUEST UNSUCCESSFUL - NO USER WITH ID:" + id + " FOUND");
            throw new NoSuchUserException("There is no such user");
        }
        userStorage.delete(id);
        log.debug("DELETE REQUEST SUCCESSFUL - USER WITH ID:" + id + " DELETED");
    }

    public void deleteAll() {
        idCounter = 1;
        userStorage.deleteAll();
        log.debug("DELETE REQUEST SUCCESSFUL - ALL USERS DELETED - ID COUNTER RESET");
    }

    public void addFriend(long id, long friendId) {
        if (!userStorage.contains(id) || !userStorage.contains(friendId)) {
            log.warn("PUT REQUEST UNSUCCESSFUL - " + "ONE OF USERS ID:" + id + " AND ID:" + friendId + "NOT FOUND - CANNOT MAKE FRIENDS");
            throw new NoSuchUserException("There is no such user");
        }
        userStorage.get(id).addFriend(friendId);
        userStorage.get(friendId).addFriend(id);
        log.debug("PUT REQUEST SUCCESSFUL - " + "MAKE USERS ID:" + id + " AND ID:" + friendId +  " FRIENDS SUCCESSFUL");
    }

    public void deleteFriend(long id, long friendId) {
        if (!userStorage.contains(id) || !userStorage.contains(friendId)) {
            log.warn("DELETE REQUEST UNSUCCESSFUL - " + "ONE OF USERS ID:" + id + " AND ID:" + friendId + "NOT FOUND - CANNOT DELETE FRIEND");
            throw new NoSuchUserException("There is no such user");
        }
        userStorage.get(id).deleteFriend(friendId);
        log.debug("DELETE REQUEST SUCCESSFUL - " + "USERS ID:" + id + " AND ID:" + friendId + " ARE NOT FRIENDS");
    }

    public Collection<User> getFriends(long id) {
        Collection<User> friends = new ArrayList<>();
        for (long friend : userStorage.get(id).getFriends()) {
            friends.add(userStorage.get(friend));
        }
        log.debug("GET REQUEST SUCCESSFUL - GET ALL USER ID:" + id + " FRIENDS");
        return friends;
    }

    public Collection<User> getCommonFriends(long id, long friendId) {
        Collection<User> commonFriends = new ArrayList<>();
        for (long friend : userStorage.get(id).getFriends()) {
            if (userStorage.get(friendId).getFriends().contains(friend)) {
                commonFriends.add(userStorage.get(friend));
            }
        }
        log.debug("GET REQUEST SUCCESSFUL - GET ALL USERS ID:" + id + " AND ID:" + friendId + " COMMON FRIENDS");
        return commonFriends;
    }

    private long getNewId() {
        return idCounter++;
    }
}
