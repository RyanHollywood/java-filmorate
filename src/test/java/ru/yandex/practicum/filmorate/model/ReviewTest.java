package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ReviewTest {

    private Review review;

    @BeforeEach
    void reload() {
        review = new Review(1, "Content", 2, 3, 1, Optional.of(true));
    }

    @Test
    void isPositive() {
        assertTrue(review.isPositive());
    }

    @Test
    void getId() {
        assertEquals(1, review.getId());
    }

    @Test
    void getContent() {
        assertEquals("Content", review.getContent());
    }

    @Test
    void getUserId() {
        assertEquals(2, review.getUserId());
    }

    @Test
    void getFilmId() {
        assertEquals(3, review.getFilmId());
    }

    @Test
    void getUseful() {
        assertEquals(1, review.getUseful());
    }

    @Test
    void getIsPositive() {
        assertTrue(review.isPositive());
    }

    @Test
    void setId() {
        review.setId(2);
        assertEquals(2, review.getId());
    }

    @Test
    void setContent() {
        review.setContent("Other content");
        assertEquals("Other content", review.getContent());
    }

    @Test
    void setUserId() {
        review.setUserId(3);
        assertEquals(3, review.getUserId());
    }

    @Test
    void setFilmId() {
        review.setFilmId(4);
        assertEquals(4, review.getFilmId());
    }

    @Test
    void setUseful() {
        review.setUseful(2);
        assertEquals(2, review.getUseful());
    }

    @Test
    void setIsPositive() {
        review.setIsPositive(Optional.of(false));
        assertFalse(review.isPositive());
    }

    @Test
    void testEquals() {
        Review sameReview = new Review(1, "Content", 2, 3, 1, Optional.of(true));
        assertTrue(review.equals(sameReview));

        sameReview.setId(2);
        assertFalse(review.equals(sameReview));
    }

    @Test
    void canEqual() {
        Review otherReview = new Review(2, "Other content", 2, 3, 1, Optional.of(true));
        assertTrue(review.canEqual(otherReview));
    }

    @Test
    void testHashCode() {
        Review sameReview = new Review(1, "Content", 2, 3, 1, Optional.of(true));
        assertEquals(review.hashCode(), sameReview.hashCode());

        sameReview.setId(2);
        assertNotEquals(review.hashCode(), sameReview.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("Review(id=1, content=Content, userId=2, filmId=3, useful=1, isPositive=Optional[true])", review.toString());
    }
}