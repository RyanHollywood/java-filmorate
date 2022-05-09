package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final LocalDate CINEMA_BIRTH_DATE = LocalDate.of(1895, 12, 28);

    //Хранилище фильмов
    Map<Integer, Film> filmStorage = new HashMap<>();

    //Логгер
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

    //Добавление фильма
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isAfter(CINEMA_BIRTH_DATE)) {
            log.debug(film.getName() + "added to filmStorage");
            filmStorage.put(film.getId(), film);
        } else {
            log.warn(film.getName() + " release date should be after " + CINEMA_BIRTH_DATE);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return film;
    }

    //Обновление фильма - исправить
    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isAfter(CINEMA_BIRTH_DATE)) {
            log.debug(film.getName() + "updated in filmStorage");
            filmStorage.put(film.getId(), film);
        } else {
            log.warn(film.getName() + " release date should be after " + CINEMA_BIRTH_DATE);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return film;
    }

    //Получение списка всех фильмов
    @GetMapping
    public List<Film> filmList() {
        return new ArrayList<>(filmStorage.values());
    }
}
