package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchGenreException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.dao.GenreDaoImpl;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class GenreService {

    private final GenreDaoImpl genreDao;

    @Autowired
    public GenreService(GenreDaoImpl genreDao) {
        this.genreDao = genreDao;
    }

    public Genre getGenre(int id) {
        Genre genre = genreDao.getGenre(id);
        if (Optional.ofNullable(genre).isEmpty()) {
            log.warn("GET REQUEST UNSUCCESSFUL - NO GENRE WITH ID:" + id + " FOUND");
            throw new NoSuchGenreException("There is no such genre. Check eventId please!");
        }
        log.debug("GET REQUEST SUCCESSFUL - GENRE WITH ID:" + id + " FOUND");
        return genre;
    }

    public Collection<Genre> getAll() {
        log.debug("GET REQUEST SUCCESSFUL - GET ALL GENRES");
        return genreDao.getAllGenres();
    }
}
