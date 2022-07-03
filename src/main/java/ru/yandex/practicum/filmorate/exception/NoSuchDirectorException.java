package ru.yandex.practicum.filmorate.exception;

public class NoSuchDirectorException extends RuntimeException {
    public NoSuchDirectorException(String msg) {
        super(msg);
    }
}
