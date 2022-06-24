package ru.yandex.practicum.filmorate.model.dao;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface MpaDao {

    Collection<Mpa> getAllMpa();

    Mpa getMpa(int id);
}
