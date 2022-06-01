package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final LocalDate CINEMA_BIRTH_DATE = LocalDate.of(1895, 12, 28);

    private InMemoryFilmStorage filmStorage;

    private int idCounter = 1;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film getFilm(long id) {
        if (!filmStorage.contains(id)) {
            throw new NoSuchFilmException("There is no such film. Check id please!");
        }
        return filmStorage.get(id);
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public void addFilm(Film film) {
        if (film.getReleaseDate().isBefore(CINEMA_BIRTH_DATE)) {
            throw new FilmValidationException("Film release date should be after " +
                    CINEMA_BIRTH_DATE.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "!");
        }
        film.setId(getNewId());
        filmStorage.add(film);
    }

    public void updateFilm(Film film) {
        if (!filmStorage.contains(film.getId())) {
            throw new NoSuchFilmException("There is no such film. Check id please!");
        }
        filmStorage.update(film);
    }

    public void deleteFilm(long id) {
        if (!filmStorage.contains(id)) {
            throw new NoSuchFilmException("There is no such film. Check id please!");
        }
        filmStorage.delete(id);
    }

    public void deleteAll() {
        idCounter = 1;
        filmStorage.deleteAll();
    }

    public void addLike(long filmId, long userId) {
        if (!filmStorage.contains(filmId)) {
            throw new NoSuchFilmException("There is no such film. Check id please!");
        }
        filmStorage.get(filmId).addLike(userId);
    }

    public void deleteLike(long filmId, long userId) {
        if (!filmStorage.contains(filmId)) {
            throw new NoSuchFilmException("There is no such film. Check id please!");
        }
        if (!filmStorage.get(filmId).containsLike(userId)) {
            throw new LikeNotFoundException("Like not found");
        }
        filmStorage.get(filmId).deleteLike(userId);
    }

    public Collection<Film> getPopularByCounter(int counter) {
        Collection<Film> popular = new TreeSet<>((film1, film2) -> {
            if (film1.getLikes().size() < film2.getLikes().size()) {
                return 1;
            } else {
                return -1;
            }
        });
        popular.addAll(filmStorage.getAll());
        return popular.stream()
                .limit(counter)
                .collect(Collectors.toSet());
    }

    private int getNewId() {
        return idCounter++;
    }
}
