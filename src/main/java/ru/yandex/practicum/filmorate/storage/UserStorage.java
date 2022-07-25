package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User get(long id);

    Collection<Event> getFeed(long id);

    Collection<User> getAll();

    void add(User user);

    void update(User user);

    void delete(long id);

    void deleteAll();

    boolean contains(long id);

    long getNewId();

    void resetId();

    void addFriend(long id, long friendId);

    void deleteFriend(long id, long friendId);

    Collection<User> getFriends(long id);

    Collection<User> getCommonFriends(long id, long friendId);
}
