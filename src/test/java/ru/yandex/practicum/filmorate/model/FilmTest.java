package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

class FilmTest {

    private Film film;

    @BeforeEach
    void reloadFilm() {
        film = new Film(1, "Film", "Film description", LocalDate.of(1895, 12, 29), Duration.ofHours(1));
    }

    @Test
    void getId() {
        Assertions.assertEquals(1, film.getId());
    }

    @Test
    void getName() {
        Assertions.assertEquals("Film", film.getName());
    }

    @Test
    void getDescription() {
        Assertions.assertEquals("Film description", film.getDescription());
    }

    @Test
    void getReleaseDate() {
        Assertions.assertEquals(LocalDate.of(1895, 12, 29), film.getReleaseDate());
    }

    @Test
    void getDuration() {
        Assertions.assertEquals(Duration.ofHours(1), film.getDuration());
    }

    @Test
    void setName() {
        String newName = "New Name";
        film.setName(newName);
        Assertions.assertEquals(newName, film.getName());
    }

    @Test
    void setDescription() {
        String newDescription = "New Description";
        film.setDescription(newDescription);
        Assertions.assertEquals(newDescription, film.getDescription());
    }

    @Test
    void setReleaseDate() {
        LocalDate newReleaseDate = LocalDate.of(1895, 12, 30);
        film.setReleaseDate(newReleaseDate);
        Assertions.assertEquals(newReleaseDate, film.getReleaseDate());
    }

    @Test
    void setDuration() {
        Duration newDuration = Duration.ofHours(2);
        film.setDuration(newDuration);
        Assertions.assertEquals(newDuration, film.getDuration());
    }

    @Test
    void testEquals() {
        Film sameFilm = new Film(1, "Film", "Film description", LocalDate.of(1895, 12, 29), Duration.ofHours(1));
        Assertions.assertTrue(film.equals(sameFilm));

        sameFilm.setName("Same film");
        Assertions.assertFalse(film.equals(sameFilm));
    }

    @Test
    void canEqual() {
        Film sameFilm = new Film(1, "Same film", "Same description", LocalDate.of(1895, 12, 29), Duration.ofHours(1));
        Assertions.assertTrue(film.canEqual(sameFilm));
    }

    @Test
    void testHashCode() {
        Film sameFilm = new Film(1, "Film", "Film description", LocalDate.of(1895, 12, 29), Duration.ofHours(1));
        Assertions.assertEquals(film.hashCode(), sameFilm.hashCode());

        sameFilm.setName("Same film");
        Assertions.assertNotEquals(film.hashCode(), sameFilm.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Film(id=1, name=Film, description=Film description, releaseDate=1895-12-29, duration=PT1H)", film.toString());
    }
}