package ru.yandex.practicum.filmorate.model.dao;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;

public interface DirectorDao {

    Collection<Director> getAllDirectors();

    Director getDirector(int id);

    void addDirector(Director director);

    void updateDirector(Director director);

    void deleteDirector(int id);

    boolean contains(int id);

    int getNewId();
}
