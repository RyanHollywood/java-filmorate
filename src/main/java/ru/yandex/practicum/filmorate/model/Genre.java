package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class Genre implements Comparable<Genre> {

    @NotNull
    private int id;
    private String name;

    @Override
    public int compareTo(Genre genre) {
        return this.id - genre.getId();
    }
}
