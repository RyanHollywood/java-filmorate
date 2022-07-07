package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ManyLikesException;
import ru.yandex.practicum.filmorate.exception.NoSuchReviewException;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class ReviewDaoImpl implements ReviewDao {

    private final JdbcTemplate jdbcTemplate;

    public ReviewDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int getId() {
        SqlRowSet response = jdbcTemplate.queryForRowSet("SELECT * FROM reviews ORDER BY review_id DESC LIMIT 1;");
        if (response.next()) {
            return response.getInt("review_id") + 1;
        }
        return 1;
    }

    @Override
    public Review createReview(Review review) {
        if (review.getId() == 0) {
            review.setId(getId());
        }
        jdbcTemplate.update("MERGE INTO reviews(review_id,content,is_positive,user_id,film_id,useful) VALUES(?,?,?,?,?,?)",
                review.getId(), review.getContent(), review.isPositive(), review.getUserId(), review.getFilmId(), review.getUseful());
        return review;
    }

    @Override
    public void removeReview(int id) {
        jdbcTemplate.update("DELETE FROM reviews WHERE review_id = ?", id);
    }

    @Override
    public Review getReviewById(int id) {
        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet("SELECT * FROM reviews WHERE review_id = ?", id);
        if (reviewRows.next()) {
            Review review = new Review(
                    reviewRows.getInt("review_id"),
                    reviewRows.getString("content"),
                    reviewRows.getInt("user_id"),
                    reviewRows.getInt("film_id"),
                    reviewRows.getInt("useful"),
                    Optional.of(reviewRows.getBoolean("is_positive"))
            );
            return review;
        }
        throw new NoSuchReviewException("Review with eventId = " + id + " does not exist");
    }

    @Override
    public void addLike(int reviewId, int userId) {

        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet("SELECT * FROM reviews_users WHERE review_id = ? AND user_id = ?;", reviewId, userId);
        if (reviewRows.next()) {
            throw new ManyLikesException("User with eventId = " + userId + " already has like/dislike. User can have only one like/dislike per review.");
        }

        jdbcTemplate.update("UPDATE reviews SET useful = ? WHERE review_id = ?",
                getReviewById(reviewId).getUseful() + 1, reviewId);
        jdbcTemplate.update("UPDATE reviews_users SET  review_id= ?, user_id = ?, is_like = ?;",
                reviewId, userId, true);
    }

    @Override
    public void addDisLike(int reviewId, int userId) {

        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet("SELECT * FROM reviews_users WHERE review_id = ? AND user_id = ?;", reviewId, userId);
        if (reviewRows.next()) {
            throw new ManyLikesException("User with eventId = " + userId + " already has like/dislike. User can have only one like/dislike per review.");
        }

        jdbcTemplate.update("UPDATE reviews SET useful = ? WHERE review_id = ?;",
                getReviewById(reviewId).getUseful() - 1, reviewId);
        jdbcTemplate.update("UPDATE reviews_users SET  review_id= ?, user_id = ?, is_like = ?;",
                reviewId, userId, false);
    }

    @Override
    public void removeLike(int reviewId, int userId) {
        jdbcTemplate.update("UPDATE reviews SET useful = ? WHERE review_id = ?",
                getReviewById(reviewId).getUseful() - 1, userId);
        jdbcTemplate.update("DELETE FROM reviews_users WHERE review_id = ? AND user_id = ? AND is_like = ?",
                reviewId, userId, true);
    }

    @Override
    public void removeDisLike(int reviewId, int userId) {
        jdbcTemplate.update("UPDATE reviews SET useful = ? WHERE review_id = ?",
                getReviewById(reviewId).getUseful() + 1, userId);
        jdbcTemplate.update("DELETE FROM reviews_users WHERE review_id = ? AND user_id = ? AND is_like = ?",
                reviewId, userId, false);
    }

    @Override
    public Set<Review> getAllReviews() {
        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet("SELECT * FROM reviews;");
        Set<Review> allReviews = new HashSet<>();
        while (reviewRows.next()) {
            allReviews.add(
                    new Review(
                            reviewRows.getInt("review_id"),
                            reviewRows.getString("content"),
                            reviewRows.getInt("user_id"),
                            reviewRows.getInt("film_id"),
                            reviewRows.getInt("useful"),
                            Optional.of(reviewRows.getBoolean("is_positive"))
                    ));
        }
        return allReviews;
    }

    @Override
    public void update(Review review) {
        review.setFilmId(getReviewById(review.getId()).getFilmId());
        review.setUseful(getReviewById(review.getId()).getUseful());
        review.setUserId(getReviewById(review.getId()).getUserId());
        jdbcTemplate.update("MERGE INTO reviews(review_id,content,is_positive,user_id,film_id,useful) VALUES(?,?,?,?,?,?)",
                review.getId(), review.getContent(), review.isPositive(), review.getUserId(), review.getFilmId(), review.getUseful());
    }

    @Override
    public Set<Review> getNegativeReviews(int filmId, int count) {
        if (count == 0) {
            count = Integer.MAX_VALUE;
        }
        Set<Review> answer = new HashSet<>();
        for (Review review : getAllReviews()) {
            if (count == 0) break;
            if (review.getFilmId() == filmId) {
                answer.add(review);
                count--;
            }
        }
        return answer;
    }
}
