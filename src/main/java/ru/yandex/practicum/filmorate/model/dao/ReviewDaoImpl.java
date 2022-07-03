package ru.yandex.practicum.filmorate.model.dao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ManyLikesException;
import ru.yandex.practicum.filmorate.exception.NoSuchReviewException;
import ru.yandex.practicum.filmorate.exception.ReviewNullException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class ReviewDaoImpl implements ReviewDao{
    private final JdbcTemplate jdbcTemplate;

    @Getter
    @Setter
    private FilmService filmService;
    @Getter
    @Setter
    private UserService userService;

    public ReviewDaoImpl(JdbcTemplate jdbcTemplate, FilmService filmService, UserService userService) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmService = filmService;
        this.userService = userService;
    }

    public int getMaxId() {
        Set<Review> reviews = getAllReviews();
        int maxId = 0;
        for (Review review : reviews) {
            if (maxId < review.getId()) maxId = review.getId();
        }
        return maxId + 1;
    }

    @Override
    public Review createReview(Review review) {
        {
            filmService.getFilm(review.getFilmId());
            userService.getUser((long) review.getUserId());
        }
        if(review.getId() == 0) review.setId(getMaxId());
        jdbcTemplate.update("MERGE INTO reviews(review_id,content,is_positive,user_id,film_id,useful) VALUES(?,?,?,?,?,?)",
                review.getId(),review.getContent(),review.isPositive(),review.getUserId(),review.getFilmId(),review.getUseful());
        return review;
    }

    @Override
    public void removeReview(int id) {
        jdbcTemplate.update("DELETE FROM reviews WHERE review_id = ?",id);
    }

    @Override
    public Review getReviewById(int id) {
        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet("SELECT * FROM reviews WHERE review_id = ?",id);
        if(reviewRows.next()) {
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
            throw new NoSuchReviewException("Review with id = " + id + " does not exist");
    }

    @Override
    public void addLike(int reviewId, int userId) {
        {
            if(getReviewById(reviewId) == null )throw new ReviewNullException("Review with id = "+reviewId+" does not exist");
            userService.getUser((long) reviewId);
        }
        {
            SqlRowSet reviewRows = jdbcTemplate.queryForRowSet("SELECT * FROM reviews_users WHERE review_id = ? AND user_id = ?;",reviewId,userId);
            if(reviewRows.next()){
                throw new ManyLikesException("User with id = "+userId+" already has like/dislike. User can have only one like/dislike per review.");
            }
        }
        jdbcTemplate.update("UPDATE reviews SET useful = ? WHERE review_id = ?",
                getReviewById(reviewId).getUseful()+1, reviewId);
        jdbcTemplate.update("UPDATE reviews_users SET  review_id= ?, user_id = ?, is_like = ?;",
                reviewId,userId,true);
    }

    @Override
    public void addDisLike(int reviewId, int userId) {
        {
            if(getReviewById(reviewId) == null )throw new ReviewNullException("Review with id = "+reviewId+" does not exist");
            userService.getUser((long) reviewId);
        }
        {
            SqlRowSet reviewRows = jdbcTemplate.queryForRowSet("SELECT * FROM reviews_users WHERE review_id = ? AND user_id = ?;",reviewId,userId);
            if(reviewRows.next()){
                throw new ManyLikesException("User with id = "+userId+" already has like/dislike. User can have only one like/dislike per review.");
            }
        }
        jdbcTemplate.update("UPDATE reviews SET useful = ? WHERE review_id = ?;",
                getReviewById(reviewId).getUseful()-1, reviewId);
        jdbcTemplate.update("UPDATE reviews_users SET  review_id= ?, user_id = ?, is_like = ?;",
                reviewId,userId,false);
    }

    @Override
    public void removeLike(int reviewId, int userId) {
        jdbcTemplate.update("UPDATE reviews SET useful = ? WHERE review_id = ?",
                getReviewById(reviewId).getUseful()-1, userId);
        jdbcTemplate.update("DELETE FROM reviews_users WHERE review_id = ? AND user_id = ? AND is_like = ?",
                reviewId,userId,true);
    }

    @Override
    public void removeDisLike(int reviewId, int userId) {
        jdbcTemplate.update("UPDATE reviews SET useful = ? WHERE review_id = ?",
                getReviewById(reviewId).getUseful()+1, userId);
        jdbcTemplate.update("DELETE FROM reviews_users WHERE review_id = ? AND user_id = ? AND is_like = ?",
                reviewId,userId,false);
    }

    @Override
    public Set<Review> getAllReviews() {
        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet("SELECT * FROM reviews;");
        Set<Review> allReviews = new HashSet<>();
        while (reviewRows.next()){
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
        removeReview(review.getId());
        createReview(review);
    }

    @Override
    public Set<Review> getNReviews(int filmId, int count) {
        if(count == 0)count = Integer.MAX_VALUE;
        Set<Review> answer = new HashSet<>();
        for(Review review: getAllReviews()){
            if(count==0)break;
            if(review.getFilmId() == filmId){
                answer.add(review);
                count--;
            }
        }
        return answer;
    }
}
