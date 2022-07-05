package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ReviewNullException;
import ru.yandex.practicum.filmorate.exception.ReviewValidationException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.dao.ReviewDaoImpl;

import java.util.Set;

@Slf4j
@Service
public class ReviewService {
    private final ReviewDaoImpl reviewDbStorage;
    private EventService eventService;

    @Autowired
    public ReviewService(ReviewDaoImpl reviewDbStorage, EventService eventService) {
        this.reviewDbStorage = reviewDbStorage;
        this.eventService = eventService;
    }

    public ReviewDaoImpl getReviewDbStorage() {
        return reviewDbStorage;
    }
    public Review createReview(Review review){
        //переделать с логгированием
        if (review.getId() < 0) {
            log.warn("CREATE REVIEW UNSUCCESSFUL - REVIEW ID VALIDATION ERROR");
            throw new ReviewValidationException("WRONG Review eventId: " + review.getId() + ".");
        }
        if (review.getFilmId() < 0) {
            log.warn("CREATE REVIEW UNSUCCESSFUL - FILM ID VALIDATION ERROR");
            throw new ReviewValidationException("WRONG Film eventId: " + review.getFilmId() + ".");
        }
        if (review.getUserId() < 0) {
            log.warn("CREATE REVIEW UNSUCCESSFUL - USER ID VALIDATION ERROR");
            throw new ReviewValidationException("WRONG User eventId: " + review.getUserId() + ".");
        }
        if (review.getFilmId() == 0) {
            log.warn("CREATE REVIEW UNSUCCESSFUL - FILM ID VALIDATION ERROR");
            throw new ReviewNullException("WRONG Film eventId: " + review.getFilmId() + ".");
        }
        if (review.getUserId() == 0) {
            log.warn("CREATE REVIEW UNSUCCESSFUL - USER ID VALIDATION ERROR");
            throw new ReviewNullException("WRONG User eventId: "+ review.getUserId() + ".");
        }
        if (review.getContent().isEmpty()) {
            log.warn("CREATE REVIEW UNSUCCESSFUL - CONTENT VALIDATION ERROR");
            throw new ReviewNullException("Content is empty.");
        }
        if (review.getIsPositive().isEmpty()) {
            log.warn("CREATE REVIEW UNSUCCESSFUL - POSITIVE FIELD VALIDATION ERROR");
            throw new ReviewNullException("isPositive is null.");
        }
        eventService.addEvent(new Event(null, review.getUserId(), reviewDbStorage.getId(), "REVIEW", "ADD", System.currentTimeMillis()));
        log.debug("CREATE REVIEW SUCCESSFUL - REVIEW WITH ID:" + review.getId() + " CREATED");
        return getReviewDbStorage().createReview(review);
    }

    public void removeReview(int id){
        Review review = getReviewById(id);
        getReviewDbStorage().removeReview(id);
        eventService.addEvent(new Event(null, review.getUserId(), review.getId(), "REVIEW", "REMOVE", System.currentTimeMillis()));
        log.debug("CREATE REMOVE SUCCESSFUL - REVIEW WITH ID:" + review.getId() + " REMOVED");
    }

    public Review getReviewById(int id){
        log.debug("GET REVIEW SUCCESSFUL - REVIEW WITH ID:" + id + " FOUND");
        return getReviewDbStorage().getReviewById(id);
    }

    public void addLike(int reviewId, int userId){
        log.debug("ADD LIKE REVIEW SUCCESSFUL - REVIEW WITH ID:" + reviewId + " LIKED BY USER WITH ID:" + userId);
        getReviewDbStorage().addLike(reviewId,userId);
    }

    public void addDisLike(int reviewId, int userId){
        log.debug("ADD DISLIKE REVIEW SUCCESSFUL - REVIEW WITH ID:" + reviewId + " DISLIKED BY USER WITH ID:" + userId);
        getReviewDbStorage().addDisLike(reviewId,userId);
    }

    public void removeLike(int reviewId, int userId){
        log.debug("REMOVE LIKE REVIEW SUCCESSFUL - REVIEW WITH ID:" + reviewId + " NO MORE LIKED BY USER WITH ID:" + userId);
        getReviewDbStorage().removeLike(reviewId,userId);
    }

    public void removeDisLike(int reviewId, int userId){
        log.debug("REMOVE DISLIKE REVIEW SUCCESSFUL - REVIEW WITH ID:" + reviewId + " NO MORE LIKED BY USER WITH ID:" + userId);
        getReviewDbStorage().removeDisLike(reviewId, userId);
    }

    public Set<Review> getAllReviews(){
        log.debug("GET ALL REVIEWS SUCCESSFUL");
        return getReviewDbStorage().getAllReviews();
    }

    public void update(Review review){
        getReviewDbStorage().update(review);
        eventService.addEvent(new Event(null, review.getUserId(), review.getId(), "REVIEW", "UPDATE", System.currentTimeMillis()));
        log.debug("UPDATE REVIEW SUCCESSFUL - REVIEW WITH ID:" + review.getId() + " UPDATED");
    }

    public Set<Review> getNegativeReviews(int filmId, int count){
        log.debug("GET ALL NEGATIVE REVIEWS SUCCESSFUL");
        return getReviewDbStorage().getNegativeReviews(filmId,count);
    }
}
