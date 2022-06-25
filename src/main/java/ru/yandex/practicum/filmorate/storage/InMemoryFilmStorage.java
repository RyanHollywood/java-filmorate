package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Long, Film> filmMap = new HashMap<>();

    private long idCounter = 1;

    @Override
    public Film get(long id) {
        return filmMap.get(id);
    }

    @Override
    public Collection<Film> getAll() {
        return filmMap.values();
    }

    @Override
    public void add(Film film) {
        filmMap.put(film.getId(), film);
    }

    @Override
    public void update(Film film) {
        filmMap.replace(film.getId(), film);
    }

    @Override
    public void delete(long id) {
        filmMap.remove(id);
    }

    @Override
    public void deleteAll() {
        filmMap.clear();
    }

    @Override
    public boolean contains(long id) {
        return filmMap.containsKey(id);
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
    public void addLike(long filmId, long userId) {
        filmMap.get(filmId).addLike(userId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        filmMap.get(filmId).deleteLike(userId);
    }

    @Override
    public boolean containsLike(long filmId, long userId) {
        return filmMap.get(filmId).containsLike(userId);
    }
}
