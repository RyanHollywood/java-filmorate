package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchMpaException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.MpaDaoImpl;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class MpaService {

    private final MpaDaoImpl mpaDao;

    @Autowired
    public MpaService(MpaDaoImpl mpaDao) {
        this.mpaDao = mpaDao;
    }

    public Mpa getMpa(int id) {
        Mpa mpa = mpaDao.getMpa(id);
        if (Optional.ofNullable(mpa).isEmpty()) {
            log.warn("GET REQUEST UNSUCCESSFUL - NO MPA WITH ID:" + id + " FOUND");
            throw new NoSuchMpaException("There is no such mpa. Check eventId please!");
        }
        log.debug("GET REQUEST SUCCESSFUL - MPA WITH ID:" + id + " FOUND");
        return mpa;
    }

    public Collection<Mpa> getAll() {
        log.debug("GET REQUEST SUCCESSFUL - GET ALL MPA");
        return mpaDao.getAllMpa();
    }
}
