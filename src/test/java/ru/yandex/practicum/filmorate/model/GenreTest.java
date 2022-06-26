package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenreTest {

    private Genre genre;

    @BeforeEach
    void reload() {
        genre = new Genre(1, "Комедия");
    }

    @Test
    void getId() {
        assertEquals(1, genre.getId());
    }

    @Test
    void getName() {
        assertEquals("Комедия", genre.getName());
    }

    @Test
    void setId() {
        genre.setId(2);
        assertEquals(2, genre.getId());
    }

    @Test
    void setName() {
        genre.setName("Драма");
        assertEquals("Драма", genre.getName());
    }

    @Test
    void testEquals() {
        Genre sameGenre = new Genre(1, "Комедия");
        assertTrue(genre.equals(sameGenre));

        sameGenre.setId(2);
        assertFalse(genre.equals(sameGenre));
    }

    @Test
    void canEqual() {
        Genre sameGenre = new Genre(2, "Драма");
        assertTrue(genre.canEqual(sameGenre));
    }

    @Test
    void testHashCode() {
        Genre sameGenre = new Genre(1, "Комедия");
        assertEquals(genre.hashCode(), sameGenre.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Genre(id=1, name=Комедия)", genre.toString());
    }
}