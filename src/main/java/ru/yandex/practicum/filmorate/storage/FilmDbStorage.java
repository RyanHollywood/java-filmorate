package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public Film get(long id) {
        return null;
    }

    @Override
    public Collection<Film> getAll() {
        return null;
    }

    @Override
    public void add(Film film) {

    }

    @Override
    public void update(Film film) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public boolean contains(long id) {
        return false;
    }
}
