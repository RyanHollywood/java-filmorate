package ru.yandex.practicum.filmorate.exception;

public class ManyLikesException extends RuntimeException{
    public ManyLikesException(String message) {
        super(message);
    }
}
