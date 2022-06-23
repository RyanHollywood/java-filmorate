package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
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
