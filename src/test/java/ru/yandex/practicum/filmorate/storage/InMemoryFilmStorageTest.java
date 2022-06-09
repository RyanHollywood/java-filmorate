package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

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
        assertEquals(film, filmStorage.get(film.getId()));
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

        assertArrayEquals(excpected.toArray(), actual.toArray());
    }

    @Test
    void update() {
        Film updatedFilm = new Film(1, "UpdatedFilm", "UpdatedFilm description", LocalDate.of(1895, 12, 29), Duration.ofHours(1));
        filmStorage.add(film);
        filmStorage.update(updatedFilm);
        assertEquals(updatedFilm, filmStorage.get(film.getId()));
    }

    @Test
    void delete() {
        filmStorage.add(film);
        filmStorage.delete(film.getId());
        assertNull(filmStorage.get(film.getId()));
    }

    @Test
    void deleteAll() {
        Film anotherFilm = new Film(2, "AnotherFilm", "AnotherFilm description", LocalDate.of(1895, 12, 29), Duration.ofHours(1));
        filmStorage.add(film);
        filmStorage.add(anotherFilm);
        filmStorage.deleteAll();
        assertEquals("[]", filmStorage.getAll().toString());
    }

    @Test
    void contains() {
        filmStorage.add(film);
        assertTrue(filmStorage.contains(film.getId()));
    }
}