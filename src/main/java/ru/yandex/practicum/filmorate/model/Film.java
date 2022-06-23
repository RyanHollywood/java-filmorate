package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.hibernate.validator.constraints.time.DurationMin;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    @NotNull
    private long id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    @Size(max = 200)
    private String description;

    @NotNull
    @Past
    private LocalDate releaseDate;

    @NotNull
    @DurationMin(nanos = 0)
    private Duration duration;

    @NotNull
    private Mpa mpa;

    @NotNull
    private Genre[] genres;

    private Set<Long> likes;

    public Film(long id, String name, String description, LocalDate releaseDate, Duration duration, Mpa mpa, Genre[] genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;
        likes = new HashSet<>();
    }

    public void addLike(Long id) {
        likes.add(id);
    }

    public void deleteLike(Long id) {
        likes.remove(id);
    }

    public boolean containsLike(Long id) {
        return likes.contains(id);
    }
}
