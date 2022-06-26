package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.exception.LikeNotFoundException;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final LocalDate CINEMA_BIRTH_DATE = LocalDate.of(1895, 12, 28);

    private FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
    //public FilmService(@Qualifier("inMemoryFilmStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film getFilm(long id) {
        if (!filmStorage.contains(id)) {
            log.warn("GET REQUEST UNSUCCESSFUL - NO FILM WITH ID:" + id + " FOUND");
            throw new NoSuchFilmException("There is no such film. Check id please!");
        }
        log.debug("GET REQUEST SUCCESSFUL - FILM WITH ID:" + id + " FOUND");
        return filmStorage.get(id);
    }

    public Collection<Film> getAll() {
        log.debug("GET REQUEST SUCCESSFUL - GET ALL FILM");
        return filmStorage.getAll();
    }

    public Film addFilm(Film film) {
        if (film.getReleaseDate().isBefore(CINEMA_BIRTH_DATE)) {
            log.warn("POST REQUEST UNSUCCESSFUL - FILM " + film.getName() + " SHOULD HAVE RELEASE DATE AFTER " + CINEMA_BIRTH_DATE);
            throw new FilmValidationException("Film release date should be after " +
                    CINEMA_BIRTH_DATE.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "!");
        }
        film.setId(getNewId());
        filmStorage.add(film);
        log.debug("POST REQUEST SUCCESSFUL - FILM WITH ID:" + film.getId() + " CREATED");
        return film;
    }

    public Film updateFilm(Film film) {
        if (!filmStorage.contains(film.getId())) {
            log.warn("PUT REQUEST UNSUCCESSFUL - NO FILM WITH ID:" + film.getId() + " FOUND");
            throw new NoSuchFilmException("There is no such film. Check id please!");
        }
        filmStorage.update(film);
        log.debug("PUT REQUEST SUCCESSFUL - FILM WITH ID:" + film.getId() + " UPDATED");
        return film;
    }

    public void deleteFilm(long id) {
        if (!filmStorage.contains(id)) {
            log.warn("DELETE REQUEST UNSUCCESSFUL - NO FILM WITH ID:" + id + " FOUND");
            throw new NoSuchFilmException("There is no such film. Check id please!");
        }
        filmStorage.delete(id);
        log.debug("DELETE REQUEST SUCCESSFUL - FILM WITH ID:" + id + " DELETED");
    }

    public void deleteAll() {
        filmStorage.resetId();
        filmStorage.deleteAll();
        log.debug("DELETE REQUEST SUCCESSFUL - ALL FILMS DELETED - ID COUNTER RESET");
    }

    public void addLike(long filmId, long userId) {
        if (!filmStorage.contains(filmId)) {
            log.warn("PUT REQUEST UNSUCCESSFUL - NO FILM WITH ID:" + filmId + " FOUND - CANNOT ADD LIKE FROM USER ID:" + userId);
            throw new NoSuchFilmException("There is no such film. Check id please!");
        }
        filmStorage.addLike(filmId, userId);
        log.debug("PUT REQUEST SUCCESSFUL - FILM WITH ID:" + filmId + " LIKED BY USER WITH ID:" + userId);
    }

    public void deleteLike(long filmId, long userId) {
        if (!filmStorage.contains(filmId)) {
            log.warn("DELETE REQUEST UNSUCCESSFUL - NO FILM WITH ID:" + filmId + " FOUND");
            throw new NoSuchFilmException("There is no such film. Check id please!");
        }
        if (!filmStorage.containsLike(filmId, userId)) {
            log.warn("DELETE REQUEST UNSUCCESSFUL - NO LIKE FOR FILM WITH ID:" + filmId + " FROM USER ID:" + userId + " FOUND");
            throw new LikeNotFoundException("Like not found");
        }
        filmStorage.deleteLike(filmId, userId);
        log.debug("DELETE REQUEST SUCCESSFUL - LIKE FOR FILM WITH ID:" + filmId + "FROM USER ID:" + userId + " DELETED");
    }

    public Collection<Film> getPopularByCounter(int counter) {
        Collection<Film> popular = new TreeSet<>((film1, film2) -> {
            if (film1.getLikes().size() < film2.getLikes().size()) {
                return 1;
            } else {
                return -1;
            }
        });
        popular.addAll(filmStorage.getAll());
        log.debug("GET REQUEST SUCCESSFUL - GET " + counter + " MOST POPULAR FILMS");
        return popular.stream()
                .limit(counter)
                .collect(Collectors.toSet());
    }

    private long getNewId() {
        return filmStorage.getNewId();
    }
}
