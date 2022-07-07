package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reviews")
@Validated
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;


    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public List<Review> show(@RequestParam Map<String,String> requestParams) {
        String count = requestParams.get("count");
        String filmId = requestParams.get("filmId");
        if(count==null & filmId == null) {
            List<Review> answer = reviewService.getAllReviews().stream()
                    .sorted(Comparator.comparing(Review::getUseful)).collect(Collectors.toList());
            Collections.reverse(answer);
            return answer;
        }
        if (filmId == null & count!=null) {
            List<Review> answer = reviewService.getAllReviews().stream().limit(Integer.parseInt(count))
                    .sorted(Comparator.comparing(Review::getUseful)).collect(Collectors.toList());
            Collections.reverse(answer);
            return answer;
        }
        if (count != null) {
            List<Review> answer = new ArrayList<>(reviewService.getNegativeReviews(Integer.parseInt(filmId),
                    Integer.parseInt(count)));
            Collections.reverse(answer);
            return answer;
        }
        else {
            List<Review> answer = new ArrayList<>(reviewService.getNegativeReviews(Integer.parseInt(filmId),0));
            Collections.reverse(answer);
            return answer;
        }
    }


    @PostMapping
    public Review create(@Valid @RequestBody Review review) {
        return reviewService.createReview(review);
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        reviewService.update(review);
        return review;
    }

    @GetMapping("/{id}")
    public Review getById(@PathVariable int id) {
        return reviewService.getReviewById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable int id) {
        reviewService.removeReview(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        reviewService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        reviewService.removeLike(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDislike(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        reviewService.addDisLike(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void removeDislike(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        reviewService.removeDisLike(id, userId);
    }
    @GetMapping("?filmId={filmId}&count={count}")
    public Set<Review> getNReviews(@PathVariable("filmId") int filmId, @PathVariable("count") int count){
        return reviewService.getNegativeReviews(filmId,count);
    }
}
