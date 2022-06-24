package ru.yandex.practicum.filmorate.exception;

public class NoSuchMpaException extends RuntimeException{
    public NoSuchMpaException(String msg) {
        super(msg);
    }
}