package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

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
    public Collection<Film> getPopular(int quantity) {
        Collection<Film> popular = new TreeSet<>((film1, film2) -> {
            if (film1.getLikes().size() < film2.getLikes().size()) {
                return 1;
            } else {
                return -1;
            }
        });
        popular.addAll(filmMap.values());
        return popular.stream()
                .limit(quantity)
                .collect(Collectors.toSet());
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

    @Override
    public List<Film> getRecommendations(long userId) {
        return null;
    }
}
