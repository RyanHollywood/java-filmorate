package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {

    private Film film;

    @BeforeEach
    void reload() {
        film = new Film(1, "Film", "Film description", LocalDate.of(1895, 12, 29),
                Duration.ofHours(1), null);
    }

    @Test
    void getId() {
        assertEquals(1, film.getId());
    }

    @Test
    void getName() {
        assertEquals("Film", film.getName());
    }

    @Test
    void getDescription() {
        assertEquals("Film description", film.getDescription());
    }

    @Test
    void getReleaseDate() {
        assertEquals(LocalDate.of(1895, 12, 29), film.getReleaseDate());
    }

    @Test
    void getDuration() {
        assertEquals(Duration.ofHours(1), film.getDuration());
    }

    @Test
    void setName() {
        String newName = "New Name";
        film.setName(newName);
        assertEquals(newName, film.getName());
    }

    @Test
    void setDescription() {
        String newDescription = "New Description";
        film.setDescription(newDescription);
        assertEquals(newDescription, film.getDescription());
    }

    @Test
    void setReleaseDate() {
        LocalDate newReleaseDate = LocalDate.of(1895, 12, 30);
        film.setReleaseDate(newReleaseDate);
        assertEquals(newReleaseDate, film.getReleaseDate());
    }

    @Test
    void setDuration() {
        Duration newDuration = Duration.ofHours(2);
        film.setDuration(newDuration);
        assertEquals(newDuration, film.getDuration());
    }

    @Test
    void testEquals() {
        Film sameFilm = new Film(1, "Film", "Film description", LocalDate.of(1895, 12, 29),
                Duration.ofHours(1), null);
        assertTrue(film.equals(sameFilm));

        sameFilm.setName("Same film");
        assertFalse(film.equals(sameFilm));
    }

    @Test
    void canEqual() {
        Film sameFilm = new Film(1, "Same film", "Same description", LocalDate.of(1895, 12, 29),
                Duration.ofHours(1), null);
        assertTrue(film.canEqual(sameFilm));
    }

    @Test
    void testHashCode() {
        Film sameFilm = new Film(1, "Film", "Film description", LocalDate.of(1895, 12, 29),
                Duration.ofHours(1), null);
        assertEquals(film.hashCode(), sameFilm.hashCode());

        sameFilm.setName("Same film");
        assertNotEquals(film.hashCode(), sameFilm.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Film(id=1, name=Film, description=Film description, releaseDate=1895-12-29, duration=PT1H, mpa=null, directors=null, genres=null, likes=[])", film.toString());
    }
}