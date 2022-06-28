package ru.yandex.practicum.filmorate.model.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Component
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Genre> getAllGenres() {
        String query = "SELECT * FROM genres;";
        return jdbcTemplate.query(query, (rs, rowNum) -> new Genre(
                rs.getInt("genre_id"),
                rs.getString("genre_name")));
    }

    @Override
    public Genre getGenre(int id) {
        SqlRowSet response = jdbcTemplate.queryForRowSet("SELECT * FROM genres WHERE genre_id = ?;", id);
        if (response.next()) {
            return new Genre(response.getInt("genre_id"), response.getString("genre_name"));
        } else {
            return null;
        }
    }
}
