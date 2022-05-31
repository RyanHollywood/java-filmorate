package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film get(int id);

    Collection<Film> getAll();

    void add(Film film);

    void update(Film film);

    void delete(int id);

    boolean contains(int id);
}
