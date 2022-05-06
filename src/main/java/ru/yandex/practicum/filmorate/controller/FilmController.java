package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    //Хранилище фильмов
    Map<Integer, Film> filmStorage = new HashMap<>();

    //Добавление фильма
    @PostMapping
    public Film create(@RequestBody Film film) {
        filmStorage.put(film.getId(), film);
        return film;
    }

    //Обновление фильма
    @PutMapping
    public Film update(@RequestBody Film film) {
        filmStorage.put(film.getId(), film);
        return film;
    }

    //Получение списка всех фильмов
    @GetMapping
    public List<Film> filmList() {
        return new ArrayList<>(filmStorage.values());
    }

    private boolean userValidation(Film film) {
        return true;
    }
}
