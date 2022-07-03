package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class Director implements Comparable<Director>{
    @NotNull
    private int id;
    @NotBlank
    private String name;

    @Override
    public int compareTo(Director director) {
        return this.id - director.getId();
    }
}
