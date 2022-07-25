package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectorTest {

    private Director director;

    @BeforeEach
    void reload() {
        director = new Director(1, "Director");
    }

    @Test
    void compareTo() {
        Director otherDirector = new Director(2, "Other director");
        assertTrue(director.compareTo(otherDirector) < 0);
    }

    @Test
    void getId() {
        assertEquals(1, director.getId());
    }

    @Test
    void getName() {
        assertEquals("Director", director.getName());
    }

    @Test
    void setId() {
        director.setId(2);
        assertEquals(2, director.getId());
    }

    @Test
    void setName() {
        director.setName("Other director");
        assertEquals("Other director", director.getName());
    }

    @Test
    void testEquals() {
        Director sameDirector = new Director(1, "Director");
        assertTrue(director.equals(sameDirector));
    }

    @Test
    void canEqual() {
        Director otherDirector = new Director(2, "Other director");
        assertTrue(director.canEqual(otherDirector));
    }

    @Test
    void testHashCode() {
        Director sameDirector = new Director(1, "Director");
        assertEquals(director.hashCode(), sameDirector.hashCode());

        sameDirector.setId(2);
        assertNotEquals(director.hashCode(), sameDirector.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Director(id=1, name=Director)", director.toString());
    }
}