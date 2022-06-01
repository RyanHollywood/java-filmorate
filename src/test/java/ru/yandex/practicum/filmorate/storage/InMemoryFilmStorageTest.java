package ru.yandex.practicum.filmorate.storage;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryFilmStorageTest {

    private InMemoryFilmStorage filmStorage;
    private Film film;

    @BeforeEach
    void reload() {
        filmStorage = new InMemoryFilmStorage();
        film = new Film(1, "Film", "Film description", LocalDate.of(1895, 12, 29), Duration.ofHours(1));
    }

    @Test
    void addAndGet() {
        filmStorage.add(film);
        Assertions.assertEquals(film, filmStorage.get(film.getId()));
    }

    @Test
    void getAll() {
        Film anotherFilm = new Film(2, "AnotherFilm", "AnotherFilm description", LocalDate.of(1895, 12, 29), Duration.ofHours(1));
        filmStorage.add(film);
        filmStorage.add(anotherFilm);

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
        actual.addAll(filmStorage.getAll());

        Assertions.assertArrayEquals(excpected.toArray(), actual.toArray());
    }

    @Test
    void update() {
        Film updatedFilm = new Film(1, "UpdatedFilm", "UpdatedFilm description", LocalDate.of(1895, 12, 29), Duration.ofHours(1));
        filmStorage.add(film);
        filmStorage.update(updatedFilm);
        Assertions.assertEquals(updatedFilm, filmStorage.get(film.getId()));
    }

    @Test
    void delete() {
        filmStorage.add(film);
        filmStorage.delete(film.getId());
        Assertions.assertNull(filmStorage.get(film.getId()));
    }

    @Test
    void deleteAll() {
        Film anotherFilm = new Film(2, "AnotherFilm", "AnotherFilm description", LocalDate.of(1895, 12, 29), Duration.ofHours(1));
        filmStorage.add(film);
        filmStorage.add(anotherFilm);
        filmStorage.deleteAll();
        Assertions.assertEquals("[]", filmStorage.getAll().toString());
    }

    @Test
    void contains() {
        filmStorage.add(film);
        Assertions.assertTrue(filmStorage.contains(film.getId()));
    }
}