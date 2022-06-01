package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> userMap = new HashMap<>();

    @Override
    public User get(Long id) {
        return userMap.get(id);
    }

    @Override
    public Collection<User> getAll() {
        return userMap.values();
    }

    @Override
    public void add(Long id, User user) {
        userMap.put(id, user);
    }

    @Override
    public void update(Long id, User user) {
        userMap.put(id, user);
    }

    @Override
    public void delete(Long id) {
        userMap.remove(id);
    }

    @Override
    public void deleteAll() {
        userMap.clear();
    }

    @Override
    public boolean contains(Long id) {
        return userMap.containsKey(id);
    }
}
