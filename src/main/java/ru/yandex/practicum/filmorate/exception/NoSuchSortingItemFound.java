package ru.yandex.practicum.filmorate.exception;

public class NoSuchSortingItemFound extends RuntimeException{
    public NoSuchSortingItemFound(String msg) {
        super(msg);
    }
}
