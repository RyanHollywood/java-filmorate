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
}
