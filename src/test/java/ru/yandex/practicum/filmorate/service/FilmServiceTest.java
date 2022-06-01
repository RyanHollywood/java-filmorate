package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {

    private FilmService filmService;

    private Film film;

    @BeforeEach
    void reload() {
        filmService = new FilmService(new InMemoryFilmStorage());
        film = new Film(1L, "Film", "Film description", LocalDate.of(1895, 12, 29), Duration.ofHours(1));
    }

    @Test
    void addAndGetFilm() {
        NoSuchFilmException exception = Assertions.assertThrows(NoSuchFilmException.class, () ->
                filmService.getFilm(film.getId())
        );
        Assertions.assertEquals("There is no such film. Check id please!", exception.getMessage());

        filmService.addFilm(film);
        Assertions.assertEquals(film, filmService.getFilm(1L));
    }

    @Test
    void getAll() {
        Film anotherFilm = new Film(2, "AnotherFilm", "AnotherFilm description", LocalDate.of(1895, 12, 29), Duration.ofHours(1));
        filmService.addFilm(film);
        filmService.addFilm(anotherFilm);

        Collection<Film> excpected = new TreeSet<>((film1, film2) -> {
            if (film1.getId() < film2.getId()) {
                return 1;
            } else {
                return -1;
            }
        });
        excpected.add(film);
        excpected.add(anotherFilm);

        Collection<Film> actual = new TreeSet<>((film1, film2) -> {
            if (film1.getId() < film2.getId()) {
                return 1;
            } else {
                return -1;
            }
        });
        actual.addAll(filmService.getAll());

        Assertions.assertArrayEquals(excpected.toArray(), actual.toArray());
    }

    @Test
    void updateFilm() {
        Film updatedFilm = new Film(1, "UpdatedFilm", "UpdatedFilm description", LocalDate.of(1895, 12, 29), Duration.ofHours(1));
        NoSuchFilmException exception = Assertions.assertThrows(NoSuchFilmException.class, () ->
                filmService.updateFilm(updatedFilm)
        );
        Assertions.assertEquals("There is no such film. Check id please!", exception.getMessage());

        filmService.addFilm(film);
        filmService.updateFilm(updatedFilm);
        Assertions.assertEquals(updatedFilm, filmService.getFilm(film.getId()));
    }

    @Test
    void deleteFilm() {
        filmService.addFilm(film);
        filmService.deleteFilm(film.getId());
        NoSuchFilmException exception = Assertions.assertThrows(NoSuchFilmException.class, () ->
            filmService.getFilm(film.getId())
        );
        Assertions.assertEquals("There is no such film. Check id please!", exception.getMessage());
    }

    @Test
    void deleteAll() {
        Film anotherFilm = new Film(2, "AnotherFilm", "AnotherFilm description", LocalDate.of(1895, 12, 29), Duration.ofHours(1));
        filmService.addFilm(film);
        filmService.addFilm(anotherFilm);
        filmService.deleteAll();
        Assertions.assertEquals("[]", filmService.getAll().toString());
    }

    @Test
    void addAndGetLike() {
        long userId = 1;
        filmService.addFilm(film);
        filmService.addLike(film.getId(), userId);
        Assertions.assertEquals(Set.of(userId), filmService.getFilm(film.getId()).getLikes());
    }

    @Test
    void deleteLike() {
        long userId = 1;
        long anotherUserId = 2;
        filmService.addFilm(film);
        filmService.addLike(film.getId(), userId);
        filmService.addLike(film.getId(), anotherUserId);
        filmService.deleteLike(film.getId(), anotherUserId);
        Assertions.assertEquals(Set.of(userId), filmService.getFilm(film.getId()).getLikes());
    }

    @Test
    void getPopularByCounter() {
        long userId = 1;
        long anotherUserId = 2;
        filmService.addFilm(film);
        filmService.addLike(film.getId(), userId);
        filmService.addLike(film.getId(), anotherUserId);

        Film anotherFilm = new Film(2, "AnotherFilm", "AnotherFilm description", LocalDate.of(1895, 12, 29), Duration.ofHours(1));
        filmService.addFilm(anotherFilm);
        filmService.addLike(anotherFilm.getId(), userId);

        Assertions.assertEquals(Set.of(film), filmService.getPopularByCounter(1));
    }
}