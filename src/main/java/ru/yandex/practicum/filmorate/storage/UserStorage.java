package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User get(Long id);

    Collection<User> getAll();

    void add(Long id, User user);

    void update(Long id, User user);

    void delete(Long id);

    boolean contains(Long id);
}
