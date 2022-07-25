package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Slf4j
@Service
public class UserService {

    private UserStorage userStorage;
    private FilmStorage filmStorage;
    private EventService eventService;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage,
                       @Qualifier("filmDbStorage") FilmStorage filmStorage,
                       EventService eventService) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.eventService = eventService;
    }

    public User getUser(long id) {
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
        eventService.addEvent(new Event(null, id, friendId, "FRIEND", "ADD", System.currentTimeMillis()));
        log.debug("PUT REQUEST SUCCESSFUL - " + "MAKE USERS ID:" + id + " AND ID:" + friendId +  " FRIENDS SUCCESSFUL");
    }

    public void deleteFriend(long id, long friendId) {
        if (!userStorage.contains(id) || !userStorage.contains(friendId)) {
            log.warn("DELETE REQUEST UNSUCCESSFUL - " + "ONE OF USERS ID:" + id + " AND ID:" + friendId + "NOT FOUND - CANNOT DELETE FRIEND");
            throw new NoSuchUserException("There is no such user");
        }
        userStorage.deleteFriend(id, friendId);
        eventService.addEvent(new Event(null, id, friendId, "FRIEND", "REMOVE", System.currentTimeMillis()));
        log.debug("DELETE REQUEST SUCCESSFUL - " + "USERS ID:" + id + " AND ID:" + friendId + " ARE NOT FRIENDS");
    }

    public Collection<User> getFriends(long id) {
        if (!userStorage.contains(id)) {
            log.warn("GET REQUEST UNSUCCESSFUL - NO USER WITH ID:" + id + " FOUND");
            throw new NoSuchUserException("There is no such user");
        }
        log.debug("GET REQUEST SUCCESSFUL - GET ALL USER ID:" + id + " FRIENDS");
        return userStorage.getFriends(id);
    }

    public Collection<User> getCommonFriends(long id, long friendId) {
        if (!userStorage.contains(id) || !userStorage.contains(friendId)) {
            log.warn("DELETE REQUEST UNSUCCESSFUL - " + "ONE OF USERS ID:" + id + " AND ID:" + friendId + "NOT FOUND - CANNOT DELETE FRIEND");
            throw new NoSuchUserException("There is no such user");
        }
        log.debug("GET REQUEST SUCCESSFUL - GET ALL USERS ID:" + id + " AND ID:" + friendId + " COMMON FRIENDS");
        return userStorage.getCommonFriends(id, friendId);
    }

    public Collection<Event> getFeed(long id) {
        if (!userStorage.contains(id)) {
            log.warn("GET REQUEST UNSUCCESSFUL - NO USER FEED WITH ID:" + id + " FOUND");
            throw new NoSuchUserException("There is no such user");
        }
        log.debug("GET REQUEST SUCCESSFUL - USER FEED WITH ID:" + id + " FOUND");
        return userStorage.getFeed(id);
    }

    private long getNewId() {
        return userStorage.getNewId();
    }

    public Collection<Film> getRecommendations(long userId) {
        if (!userStorage.contains(userId)) {
            log.warn("GET REQUEST UNSUCCESSFUL - NO USER WITH ID:" + userId + " FOUND");
            throw new NoSuchUserException("There is no such user");
        }
        log.debug("GET REQUEST SUCCESSFUL - GET RECOMMENDATIONS");
        return filmStorage.getRecommendations(userId);
    }
}
