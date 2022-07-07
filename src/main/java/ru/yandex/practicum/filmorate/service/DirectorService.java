package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchDirectorException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.dao.DirectorDaoImpl;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class DirectorService {

    private final DirectorDaoImpl directorDao;

    @Autowired
    public DirectorService(DirectorDaoImpl directorDao) {
        this.directorDao = directorDao;
    }

    public Director getDirector(int id) {
        Director director = directorDao.getDirector(id);
        if (Optional.ofNullable(director).isEmpty()) {
            log.warn("GET REQUEST UNSUCCESSFUL - NO DIRECTOR WITH ID:" + id + " FOUND");
            throw new NoSuchDirectorException("There is no such director. Check eventId please!");
        }
        log.debug("GET REQUEST SUCCESSFUL - DIRECTOR WITH ID:" + id + " FOUND");
        return director;
    }

    public Collection<Director> getAllDirectors() {
        return directorDao.getAllDirectors();
    }

    public void addDirector(Director director) {
        directorDao.addDirector(director);
        log.debug("POST REQUEST SUCCESSFUL - DIRECTOR WITH ID:" + director.getId() + " CREATED");
    }

    public void updateDirector(Director director) {
        if (!directorDao.contains(director.getId())) {
            log.warn("PUT REQUEST UNSUCCESSFUL - NO DIRECTOR WITH ID:" + director.getId() + " FOUND");
            throw new NoSuchDirectorException("There is no such director. Check eventId please!");
        }
        directorDao.updateDirector(director);
        log.debug("PUT REQUEST SUCCESSFUL - DIRECTOR WITH ID:" + director.getId() + " UPDATED");
    }

    public void deleteDirector(int id) {
        if (!directorDao.contains(id)) {
            log.warn("DELETE REQUEST UNSUCCESSFUL - NO DIRECTOR WITH ID:" + id + " FOUND");
            throw new NoSuchDirectorException("There is no such director. Check eventId please!");
        }
        directorDao.deleteDirector(id);
        log.debug("DELETE REQUEST SUCCESSFUL - DIRECTOR WITH ID:" + id + " DELETED");
    }
}