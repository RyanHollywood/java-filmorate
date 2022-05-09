package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.time.DurationMin;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    private final int id;
    @NonNull
    @NotBlank
    private final String name;
    @NonNull
    @NotBlank
    @Size(min = 1, max = 200)
    private final String description;
    @Past
    private final LocalDate releaseDate;
    @DurationMin(nanos = 0)
    private final Duration duration;
}
