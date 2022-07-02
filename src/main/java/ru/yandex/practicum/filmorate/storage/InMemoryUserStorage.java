package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> userMap = new HashMap<>();

    private long idCounter = 1;

    @Override
    public User get(long id) {
        return userMap.get(id);
    }

    @Override
    public Collection<Event> getFeed(long id) {
        return null;
    }

    @Override
    public Collection<User> getAll() {
        return userMap.values();
    }

    @Override
    public void add(User user) {
        userMap.put(user.getId(), user);
    }

    @Override
    public void update(User user) {
        userMap.replace(user.getId(), user);
    }

    @Override
    public void delete(long id) {
        userMap.remove(id);
    }

    @Override
    public void deleteAll() {
        userMap.clear();
    }

    @Override
    public boolean contains(long id) {
        return userMap.containsKey(id);
    }

    @Override
    public long getNewId() {
        return idCounter++;
    }

    @Override
    public void resetId() {
        idCounter = 1;
    }

    @Override
    public void addFriend(long id, long friendId) {
        userMap.get(id).addFriend(friendId);
        userMap.get(friendId).addFriend(id);
    }

    @Override
    public void deleteFriend(long id, long friendId) {
        userMap.get(id).deleteFriend(friendId);
    }

    @Override
    public Collection<User> getFriends(long id) {
        Collection<User> friends = new ArrayList<>();
        for (long friend : userMap.get(id).getFriends()) {
            friends.add(userMap.get(friend));
        }
        return friends;
    }

    @Override
    public Collection<User> getCommonFriends(long id, long friendId) {
        Collection<User> commonFriends = new ArrayList<>();
        for (long friend : userMap.get(id).getFriends()) {
            if (userMap.get(friendId).getFriends().contains(friend)) {
                commonFriends.add(userMap.get(friend));
            }
        }
        return commonFriends;
    }
}
