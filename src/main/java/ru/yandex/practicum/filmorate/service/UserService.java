package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Slf4j
@Service
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
    //public UserService(@Qualifier("inMemoryUserStorage") UserStorage userStorage) {
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
        userStorage.resetId();
        userStorage.deleteAll();
        log.debug("DELETE REQUEST SUCCESSFUL - ALL USERS DELETED - ID COUNTER RESET");
    }

    public void addFriend(long id, long friendId) {
        if (!userStorage.contains(id) || !userStorage.contains(friendId)) {
            log.warn("PUT REQUEST UNSUCCESSFUL - " + "ONE OF USERS ID:" + id + " AND ID:" + friendId + "NOT FOUND - CANNOT MAKE FRIENDS");
            throw new NoSuchUserException("There is no such user");
        }
        userStorage.addFriend(id, friendId);
        log.debug("PUT REQUEST SUCCESSFUL - " + "MAKE USERS ID:" + id + " AND ID:" + friendId +  " FRIENDS SUCCESSFUL");
    }

    public void deleteFriend(long id, long friendId) {
        if (!userStorage.contains(id) || !userStorage.contains(friendId)) {
            log.warn("DELETE REQUEST UNSUCCESSFUL - " + "ONE OF USERS ID:" + id + " AND ID:" + friendId + "NOT FOUND - CANNOT DELETE FRIEND");
            throw new NoSuchUserException("There is no such user");
        }
        userStorage.deleteFriend(id, friendId);
        log.debug("DELETE REQUEST SUCCESSFUL - " + "USERS ID:" + id + " AND ID:" + friendId + " ARE NOT FRIENDS");
    }

    public Collection<User> getFriends(long id) {
        log.debug("GET REQUEST SUCCESSFUL - GET ALL USER ID:" + id + " FRIENDS");
        return userStorage.getFriends(id);
    }

    public Collection<User> getCommonFriends(long id, long friendId) {
        log.debug("GET REQUEST SUCCESSFUL - GET ALL USERS ID:" + id + " AND ID:" + friendId + " COMMON FRIENDS");
        return userStorage.getCommonFriends(id, friendId);
    }

    private long getNewId() {
        return userStorage.getNewId();
    }
}
