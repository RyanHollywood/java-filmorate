package ru.yandex.practicum.filmorate.model;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exception.NoSuchReviewException;
import ru.yandex.practicum.filmorate.exception.NoSuchUserException;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ReviewServiceTest {

    private FilmService filmService;
    private UserService userService;
    private final ReviewService reviewService;
    private final Review review = new Review
            (1, "content", 1, 1, 0, Optional.of(true));
    private final User user = new User
            (1L, "user@mail.ru", "userLogin", null, LocalDate.of(1990, 01, 01));

    private final Film film = new Film(1, "Film", "Film description", LocalDate.of(1895, 12, 29),
            Duration.ofHours(1), null);


    @BeforeEach
    void reload() {
        filmService = new FilmService(new InMemoryFilmStorage());
        userService = new UserService(new InMemoryUserStorage());
        reviewService.getStorage().setFilmService(filmService);
        reviewService.getStorage().setUserService(userService);
    }

    @AfterEach
    void removeAll() {
        try {
            filmService.deleteFilm(1);
            userService.deleteUser(1L);
            reviewService.removeReview(1);
            reviewService.removeReview(2);
            reviewService.removeLike(1, 1);
            reviewService.removeDisLike(1, 1);
        } catch (Exception e) {

        }
    }

    @Test
    void createReviewWithoutFilmAndUser() {
        Exception exception = assertThrows(NoSuchFilmException.class, () -> {
            reviewService.createReview(review);
        });

        String expectedMessage = "There is no such film. Check eventId please!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void createReviewWithoutFilm() {
        userService.addUser(user);
        Exception exception = assertThrows(NoSuchFilmException.class, () -> {
            reviewService.createReview(review);
        });

        String expectedMessage = "There is no such film. Check eventId please!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void createReviewWithoutUser() {
        filmService.addFilm(film);
        Exception exception = assertThrows(NoSuchUserException.class, () -> {
            reviewService.createReview(review);
        });

        String expectedMessage = "There is no such user";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void createReview() {
        filmService.addFilm(film);
        userService.addUser(user);
        reviewService.createReview(review);
        assertEquals(reviewService.getReviewById(review.getId()).toString(),
                "Review(eventId=1, content=content, userId=1, filmId=1, useful=0, isPositive=Optional[true])");
        reviewService.removeReview(review.getId());
    }

    @Test
    void removeReview() {
        filmService.addFilm(film);
        userService.addUser(user);
        reviewService.createReview(review);
        reviewService.removeReview(review.getId());
        Exception exception = assertThrows(NoSuchReviewException.class, () -> {
            reviewService.getReviewById(review.getId());
        });

        String expectedMessage = "Review with id = 1 does not exist";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getReviewById() {
        filmService.addFilm(film);
        userService.addUser(user);
        reviewService.createReview(review);
        assertEquals(reviewService.getReviewById(review.getId()), review);
    }


    @Test
    void addLikeWithoutReview() {
        Exception exception = assertThrows(NoSuchReviewException.class, () -> {
            reviewService.addLike(review.getId(), 2);
        });

        String expectedMessage = "Review with id = 1 does not exist";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void addLike() {
        filmService.addFilm(film);
        userService.addUser(user);
        reviewService.createReview(review);
        User user = new User
                (2L, "user@mail.ru", "userLogin", null, LocalDate.of(1990, 01, 01));
        userService.addUser(user);
        reviewService.addLike(review.getId(), (int) user.getId());
        assertEquals(reviewService.getReviewById(review.getId()).getUseful(), 1);
    }

    @Test
    void addSecondLike() {
        filmService.addFilm(film);
        userService.addUser(user);
        reviewService.createReview(review);
        reviewService.addLike(review.getId(), (int) user.getId());
        User user = new User
                (2L, "user@mail.ru", "userLogin", null, LocalDate.of(1990, 01, 01));
        userService.addUser(user);
        reviewService.addLike(review.getId(), (int) user.getId());
        assertEquals(reviewService.getReviewById(review.getId()).getUseful(), 2);
        reviewService.removeLike(review.getId(), (int) user.getId());
    }

    @Test
    void addDislikeWithoutReview() {
        Exception exception = assertThrows(NoSuchReviewException.class, () -> {
            reviewService.addDisLike(review.getId(), 2);
        });

        String expectedMessage = "Review with id = 1 does not exist";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void addDislike() {
        filmService.addFilm(film);
        userService.addUser(user);
        reviewService.createReview(review);
        reviewService.addDisLike(review.getId(), 2);
        assertEquals(reviewService.getReviewById(review.getId()).getUseful(), -1);
    }

    @Test
    void addSecondDislike() {
        filmService.addFilm(film);
        userService.addUser(user);
        reviewService.createReview(review);
        reviewService.addDisLike(review.getId(), (int) user.getId());
        User user = new User
                (2L, "user@mail.ru", "userLogin", null, LocalDate.of(1990, 01, 01));
        userService.addUser(user);
        reviewService.addDisLike(review.getId(), (int) user.getId());
        assertEquals(reviewService.getReviewById(review.getId()).getUseful(), -2);
        reviewService.removeDisLike(review.getId(), (int) user.getId());
    }

    @Test
    void removeLike() {
        filmService.addFilm(film);
        userService.addUser(user);
        reviewService.createReview(review);
        reviewService.addLike(review.getId(), (int) user.getId());
        reviewService.removeLike(review.getId(), (int) user.getId());
        assertEquals(reviewService.getReviewById(review.getId()).getUseful(), 0);
    }

    @Test
    void removeDisLike() {
        filmService.addFilm(film);
        userService.addUser(user);
        reviewService.createReview(review);
        reviewService.addDisLike(review.getId(), (int) user.getId());
        reviewService.removeDisLike(review.getId(), (int) user.getId());
        assertEquals(reviewService.getReviewById(review.getId()).getUseful(), 0);
    }

    @Test
    void getAllReviews() {
        filmService.addFilm(film);
        userService.addUser(user);
        reviewService.createReview(review);
        User user = new User
                (2L, "user@mail.ru", "userLogin", null, LocalDate.of(1990, 01, 01));
        userService.addUser(user);
        Review review = new Review
                (2, "content", 2, 1, 0, Optional.of(true));
        reviewService.createReview(review);
        assertEquals(reviewService.getAllReviews().size(), 2);
        reviewService.removeReview(2);
    }

    @Test
    void update() {
        filmService.addFilm(film);
        userService.addUser(user);
        reviewService.createReview(review);
        reviewService.update(new Review
                (1, "new content", 1, 1, 0, Optional.of(true)));
        assertEquals(reviewService.getReviewById(1).getContent(), "new content");
    }

    @Test
    void getNReviews() {
        filmService.addFilm(film);
        userService.addUser(user);
        reviewService.createReview(review);
        Review review = new Review
                (2, "content", 2, 1, 0, Optional.of(true));
        User user = new User
                (2L, "user@mail.ru", "userLogin", null, LocalDate.of(1990, 01, 01));
        userService.addUser(user);
        reviewService.createReview(review);
        reviewService.addLike(2, 2);
        reviewService.addDisLike(1, 1);
        assertEquals(reviewService.getNReviews((int) film.getId(), 1).stream().findFirst().get().toString(),
                "Review(id=2, content=content, userId=2, filmId=1, useful=1, isPositive=Optional[true])");
        reviewService.removeReview(2);
    }
}