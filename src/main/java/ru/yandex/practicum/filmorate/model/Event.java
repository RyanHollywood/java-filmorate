package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class Event implements Comparable<Event>{

    @NotNull
    private int id;

    @NotNull
    private long userId;

    @NotNull
    private long entityId;

    @NotNull
    @NotBlank
    private String eventType;

    @NotNull
    @NotBlank
    private String operation;

    @NotNull
    private long timestamp;

    @Override
    public int compareTo(Event event) {
        return this.id - event.id;
    }
}
