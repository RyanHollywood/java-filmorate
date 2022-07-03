package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ReviewNullException;
import ru.yandex.practicum.filmorate.exception.ReviewValidationException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.dao.ReviewDaoImpl;

import java.util.Set;

@Slf4j
@Service
public class ReviewService {
    private final ReviewDaoImpl storage;

    @Autowired
    public ReviewService(ReviewDaoImpl storage) {
        this.storage = storage;
    }

    public ReviewDaoImpl getStorage() {
        return storage;
    }
    public Review createReview(Review review){
        if(review.getId()<0 ) throw new ReviewValidationException("WRONG Review eventId: "+review.getId()+".");
        if(review.getFilmId()<0) throw new ReviewValidationException("WRONG Film eventId: "+review.getFilmId()+".");
        if(review.getUserId()<0) throw new ReviewValidationException("WRONG User eventId: "+review.getUserId()+".");
        if(review.getFilmId()==0) throw new ReviewNullException("WRONG Film eventId: "+review.getFilmId()+".");
        if(review.getUserId()==0) throw new ReviewNullException("WRONG User eventId: "+review.getUserId()+".");
        if(review.getContent().isEmpty()) throw new ReviewNullException("Content is empty.");
        if(review.getIsPositive().isEmpty()) throw new ReviewNullException("isPositive is null.");
        return getStorage().createReview(review);
    }

    public void removeReview(int id){
        getStorage().removeReview(id);
    }

    public Review getReviewById(int id){
        return getStorage().getReviewById(id);
    }

    public void addLike(int reviewId, int userId){
        getStorage().addLike(reviewId,userId);
    }

    public void addDisLike(int reviewId, int userId){
        getStorage().addDisLike(reviewId,userId);
    }

    public void removeLike(int reviewId, int userId){
        getStorage().removeLike(reviewId,userId);
    }

    public void removeDisLike(int dislikeId, int userId){
        getStorage().removeDisLike(dislikeId,userId);
    }

    public Set<Review> getAllReviews(){
        return getStorage().getAllReviews();
    }

    public void update(Review review){
        getStorage().update(review);
    }

    public Set<Review> getNReviews(int filmId, int count){
        return getStorage().getNReviews(filmId,count);
    }
}
