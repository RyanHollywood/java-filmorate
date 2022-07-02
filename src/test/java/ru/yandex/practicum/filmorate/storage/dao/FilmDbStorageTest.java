package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {

    private final FilmService filmService;
    private final FilmDbStorage filmDbStorage;


    @Test
    void getPopularByGenres() {
        TreeSet<Genre> genres = new TreeSet<>();





        Film film = new Film(1, "Ремба первая кровь", "Test",
                LocalDate.of(1990, 9, 15), Duration.ofSeconds(3000),
                new Mpa(1, "G"));

        Genre genre = new Genre(1, "Комедия");
        genres.add(genre);
        film.setGenres(genres);
        //filmDbStorage.addGenres(film);
        filmService.addFilm(film);

        film = new Film(2, "Ремба средняя кровь", "Test",
                LocalDate.of(1990, 9, 15), Duration.ofSeconds(3000),
                new Mpa(1, "G"));

        genres.add(genre);
        film.setGenres(genres);
        //filmDbStorage.addGenres(film);
        filmService.addFilm(film);

        film = new Film(3, "Ремба последняя", "Test",
                LocalDate.of(2000, 9, 15), Duration.ofSeconds(3000),
                new Mpa(3, "G"));
        genre = new Genre(2,"Ужасы");

        genres.add(genre);
        film.setGenres(genres);
        filmService.addFilm(film);

        Collection<Film> films = filmService.getPopularByCounter(10,1894,1);

        assertEquals(films.size(), 2);

        /**
         * Получаем фильм ТОЛЬКО по ЖАНРУ
        Размер коллекции должен быть равен 2м, т.к у 3го фильма другой жанр
         **/
    }

    @Test
    void getPopularByYear() {
        Film film = new Film(14, "Ремба первая кровь", "Test",
                LocalDate.of(1990, 9, 15), Duration.ofSeconds(3000),
                new Mpa(1, "G"));
        filmService.addFilm(film);

        film = new Film(5, "Ремба средняя кровь", "Test",
                LocalDate.of(1990, 9, 15), Duration.ofSeconds(3000),
                new Mpa(1, "G"));
        filmService.addFilm(film);

        film = new Film(6, "Ремба последняя", "Test",
                LocalDate.of(2000, 9, 15), Duration.ofSeconds(3000),
                new Mpa(3, "G"));
        filmService.addFilm(film);
        Collection<Film> films = filmService.getPopularByCounter(10,1990,1);

        assertEquals(films.size(), 2);

        /**
         * Получаем фильм ТОЛЬКО по ГОДУ
         Размер коллекции должен быть равен 2м, т.к у 3го фильма другой год выпуска
         **/
    }
}