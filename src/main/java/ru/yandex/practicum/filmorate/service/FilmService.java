package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Slf4j
@Service
public class FilmService {

    private final LocalDate CINEMA_BIRTH_DATE = LocalDate.of(1895, 12, 28);

    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private EventService eventService;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage,
                       EventService eventService) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.eventService = eventService;
    }

    public Film getFilm(long id) {
        if (!filmStorage.contains(id)) {
            log.warn("GET REQUEST UNSUCCESSFUL - NO FILM WITH ID:" + id + " FOUND");
            throw new NoSuchFilmException("There is no such film. Check eventId please!");
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
            throw new NoSuchFilmException("There is no such film. Check eventId please!");
        }
        filmStorage.update(film);
        log.debug("PUT REQUEST SUCCESSFUL - FILM WITH ID:" + film.getId() + " UPDATED");
        return film;
    }

    public void deleteFilm(long id) {
        if (!filmStorage.contains(id)) {
            log.warn("DELETE REQUEST UNSUCCESSFUL - NO FILM WITH ID:" + id + " FOUND");
            throw new NoSuchFilmException("There is no such film. Check eventId please!");
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
            throw new NoSuchFilmException("There is no such film. Check eventId please!");
        }
        filmStorage.addLike(filmId, userId);
        eventService.addEvent(new Event(null, userId, filmId, "LIKE", "ADD", System.currentTimeMillis()));
        log.debug("PUT REQUEST SUCCESSFUL - FILM WITH ID:" + filmId + " LIKED BY USER WITH ID:" + userId);
    }

    public void deleteLike(long filmId, long userId) {
        if (!filmStorage.contains(filmId)) {
            log.warn("DELETE REQUEST UNSUCCESSFUL - NO FILM WITH ID:" + filmId + " FOUND");
            throw new NoSuchFilmException("There is no such film. Check eventId please!");
        }
        if (!filmStorage.containsLike(filmId, userId)) {
            log.warn("DELETE REQUEST UNSUCCESSFUL - NO LIKE FOR FILM WITH ID:" + filmId + " FROM USER ID:" + userId + " FOUND");
            throw new LikeNotFoundException("Like not found");
        }
        filmStorage.deleteLike(filmId, userId);
        eventService.addEvent(new Event(null, userId, filmId, "LIKE", "REMOVE", System.currentTimeMillis()));
        log.debug("DELETE REQUEST SUCCESSFUL - LIKE FOR FILM WITH ID:" + filmId + "FROM USER ID:" + userId + " DELETED");
    }

    public Collection<Film> getPopularByCounter(int counter, Integer year, Integer genreId) {
        log.debug("GET REQUEST SUCCESSFUL - GET " + counter + " MOST POPULAR FILMS");
        return filmStorage.getPopular(counter, year, genreId);
    }

    public Collection<Film> getByDirectorSorted(int directorId, String sortBy) {
        Collection<Film> sortedFilms;
        if (sortBy.equals("year")) {
            sortedFilms = filmStorage.getByDirectorByYear(directorId);
        } else if (sortBy.equals("likes")) {
            sortedFilms = filmStorage.getByDirectorByLikes(directorId);
        } else {
            log.warn("GET REQUEST UNSUCCESSFUL - NO SORTING OPTION: " + sortBy + " FOUND");
            throw new NoSuchSortingItemFound("There is no such sorting item. Check parameter please!");
        }
        if (sortedFilms.isEmpty()) {
            log.warn("GET REQUEST UNSUCCESSFUL - NO DIRECTOR WITH ID:" + directorId + " FOUND");
            throw new NoSuchDirectorException("There is no such director. Check eventId please!");
        }
        log.debug("GET REQUEST SUCCESSFUL - GET DIRECTOR ID:" + directorId + " FILMS SORTED BY " + sortBy);
        return sortedFilms;
    }

    public Collection<Film> searchFilm(String query, String by) {
        log.debug("GET REQUEST SUCCESSFUL - GET " + by + " MOST SEARCH FILMS");
        return filmStorage.searchFilm(query, by);
    }

    private long getNewId() {
        return filmStorage.getNewId();
    }

    public Collection<Film> getCommon(long userId, long friendId) {
        if (!userStorage.contains(userId)) {
            log.warn("GET REQUEST UNSUCCESSFUL - NO USER WITH ID:" + userId + " FOUND");
            throw new NoSuchUserException("There is no such user");
        }
        if (!userStorage.contains(friendId)) {
            log.warn("GET REQUEST UNSUCCESSFUL - NO USER WITH ID:" + friendId + " FOUND");
            throw new NoSuchUserException("There is no such user");
        }
        log.debug("GET REQUEST SUCCESSFUL - GET COMMON FILMS");
        return filmStorage.getCommon(userId, friendId);
    }
}