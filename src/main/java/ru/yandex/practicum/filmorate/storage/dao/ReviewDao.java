package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.Set;

public interface ReviewDao {
    Review createReview(Review review);

    void removeReview(int id);

    Review getReviewById(int id);

    void addLike(int reviewId, int userId);

    void addDisLike(int reviewId, int userId);

    void removeLike(int reviewId, int userId);

    void removeDisLike(int dislikeId, int userId);

    Set<Review> getAllReviews();

    void update(Review review);

    Set<Review> getNegativeReviews(int filmId, int count);
}
