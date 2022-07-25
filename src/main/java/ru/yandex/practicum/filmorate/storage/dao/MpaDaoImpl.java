package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Component
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        String query = "SELECT * FROM mpa;";
        return jdbcTemplate.query(query, (rs, rowNum) -> new Mpa(
                rs.getInt("mpa_id"),
                rs.getString("mpa_name")));
    }

    @Override
    public Mpa getMpa(int id) {
        SqlRowSet response = jdbcTemplate.queryForRowSet("SELECT * FROM mpa WHERE mpa_id = ?;", id);
        if (response.next()) {
            return new Mpa(response.getInt("mpa_id"), response.getString("mpa_name"));
        } else {
            return null;
        }
    }
}
