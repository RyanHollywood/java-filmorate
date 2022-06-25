package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film get(long id);

    Collection<Film> getAll();

    void add(Film film);

    void update(Film film);

    void delete(long id);

    void deleteAll();

    boolean contains(long id);

    long getNewId();

    void resetId();

    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    boolean containsLike(long filmId, long userId);
}
