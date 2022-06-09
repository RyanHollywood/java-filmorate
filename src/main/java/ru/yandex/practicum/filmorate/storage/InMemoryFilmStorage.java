package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Long, Film> filmMap = new HashMap<>();

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
}
