package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Component("userDbStorage")
public class UserDbStorage implements UserStorage {
    @Override
    public User get(Long id) {
        return null;
    }

    @Override
    public Collection<User> getAll() {
        return null;
    }

    @Override
    public void add(User user) {

    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public boolean contains(Long id) {
        return false;
    }
}
