package ru.yandex.practicum.filmorate.exception;

public class FilmValidationException extends RuntimeException{
    public FilmValidationException(String msg) {
        super(msg);
    }
}
