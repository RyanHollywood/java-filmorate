package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User get(Long id);

    Collection<User> getAll();

    void add(User user);

    void update(User user);

    void delete(Long id);

    void deleteAll();

    boolean contains(Long id);

    long getNewId();

    void resetId();

    void addFriend(long id, long friendId);

    void deleteFriend(long id, long friendId);

    Collection<User> getFriends(long id);

    Collection<User> getCommonFriends(long id, long friendId);
}
