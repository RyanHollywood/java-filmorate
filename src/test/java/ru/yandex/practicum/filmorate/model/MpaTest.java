package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MpaTest {

    private Mpa mpa;

    @BeforeEach
    void reload() {
        mpa = new Mpa(1, "G");
    }

    @Test
    void getId() {
        assertEquals(1, mpa.getId());
    }

    @Test
    void getName() {
        assertEquals("G", mpa.getName());
    }

    @Test
    void setId() {
        mpa.setId(2);
        assertEquals(2, mpa.getId());
    }

    @Test
    void setName() {
        mpa.setName("PG");
        assertEquals("PG", mpa.getName());
    }

    @Test
    void testEquals() {
        Mpa sameMpa = new Mpa(1, "G");
        assertTrue(mpa.equals(sameMpa));

        sameMpa.setId(2);
        assertFalse(mpa.equals(sameMpa));
    }

    @Test
    void canEqual() {
        Mpa sameMpa = new Mpa(2, "PG");
        assertTrue(mpa.canEqual(sameMpa));
    }

    @Test
    void testHashCode() {
        Mpa sameMpa = new Mpa(1, "G");
        assertEquals(mpa.hashCode(), sameMpa.hashCode());

        sameMpa.setId(2);
        assertNotEquals(mpa.hashCode(), sameMpa.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Mpa(id=1, name=G)", mpa.toString());
    }
}