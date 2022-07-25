package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;

@Component
public class DirectorDaoImpl implements DirectorDao {

    private final JdbcTemplate jdbcTemplate;

    public DirectorDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Director> getAllDirectors() {
        String query = "SELECT * FROM directors;";
        return jdbcTemplate.query(query, (rs, rowNum) -> new Director(
                rs.getInt("director_id"),
                rs.getString("director_name")));
    }

    @Override
    public Director getDirector(int id) {
        SqlRowSet response = jdbcTemplate.queryForRowSet("SELECT * FROM directors WHERE director_id = ?;", id);
        if (response.next()) {
            return new Director(response.getInt("director_id"), response.getString("director_name"));
        } else {
            return null;
        }
    }

    @Override
    public void addDirector(Director director) {
        director.setId(getNewId());
        jdbcTemplate.update("INSERT INTO directors(director_id, director_name) VALUES (?, ?)",
                director.getId(), director.getName());
    }

    @Override
    public void updateDirector(Director director) {
        jdbcTemplate.update("MERGE INTO directors(director_id, director_name) VALUES (?, ?)",
                director.getId(), director.getName());
    }

    @Override
    public void deleteDirector(int id) {
        jdbcTemplate.update("DELETE FROM directors WHERE director_id=?", id);
    }

    @Override
    public boolean contains(int id) {
        SqlRowSet response = jdbcTemplate.queryForRowSet("SELECT * FROM directors WHERE director_id=?", id);
        return response.next();
    }

    @Override
    public int getNewId() {
        SqlRowSet response = jdbcTemplate.queryForRowSet("SELECT * FROM directors ORDER BY director_id DESC LIMIT 1;");
        if (response.next()) {
            return response.getInt("director_id") + 1;
        }
        return 1;
    }
}
