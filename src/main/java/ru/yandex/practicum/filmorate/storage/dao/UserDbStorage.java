package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

@Component("userDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public User get(Long id) {
        SqlRowSet response = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id=?;", id);
        response.next();
        return new User(response.getLong("id"), response.getString("email"), response.getString("login"),
                response.getString("name"), LocalDate.parse(response.getString("birthday")));
    }

    @Override
    public Collection<User> getAll() {
        String response = "SELECT * FROM users;";
        return jdbcTemplate.query(response, (rs, rowNum) -> new User(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                LocalDate.parse(rs.getString("birthday")))
        );
    }

    @Override
    public void add(User user) {
        jdbcTemplate.update("INSERT INTO users(id, email, login, name, birthday) VALUES(?, ?, ?, ?, ?)",
                user.getId(), user.getEmail(), user.getLogin(), user.getName(), Date.valueOf(user.getBirthday()));
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update("MERGE INTO users(id, email, login, name, birthday) VALUES(?, ?, ?, ?, ?)",
                user.getId(), user.getEmail(), user.getLogin(), user.getName(), Date.valueOf(user.getBirthday()));
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM users WHERE id=?", id);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.execute("DELETE FROM users");
    }

    @Override
    public boolean contains(Long id) {
        SqlRowSet response = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id=?", id);
        return response.next();
    }

    @Override
    public long getNewId() {
        SqlRowSet response = jdbcTemplate.queryForRowSet("SELECT * FROM users ORDER BY id DESC LIMIT 1;");
        if (response.next()) {
            return response.getLong("id") + 1;
        }
        return 1;
    }

    @Override
    public void resetId() {
        //do nothing
    }

    @Override
    public void addFriend(long id, long friendId) {
        jdbcTemplate.update("MERGE INTO friends(request_id, response_id, status_id) VALUES (?, ?, ?);", id, friendId, 1);
    }

    @Override
    public void deleteFriend(long id, long friendId) {
        jdbcTemplate.update("DELETE FROM friends WHERE request_id=? AND response_id=?", id, friendId);
    }

    @Override
    public Collection<User> getFriends(long id) {
        Collection<User> friends = new HashSet<>();
        SqlRowSet response = jdbcTemplate.queryForRowSet("SELECT fr.response_id FROM users AS u " +
                "RIGHT OUTER JOIN friends AS fr ON fr.request_id = u.id WHERE fr.request_id = ? AND fr.status_id = 1;", id);
        while(response.next()) {
            friends.add(get(response.getLong("response_id")));
        }
        return friends;
    }

    @Override
    public Collection<User> getCommonFriends(long id, long friendId) {
        return findCommon(getFriends(id), getFriends(friendId));
    }

    private Collection<User> findCommon(Collection<User> user, Collection<User> friend) {
        Collection<User> commonFriends = new HashSet<>();
        if (user.size() >= friend.size()) {
            for (User common : user) {
                if(friend.contains(common)) {
                    commonFriends.add(common);
                }
            }
        } else {
            for (User common : friend) {
                if(user.contains(common)) {
                    commonFriends.add(common);
                }
            }
        }
        return commonFriends;
    }
}
