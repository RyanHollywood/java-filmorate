package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Data
@AllArgsConstructor
public class Review {
    @NotNull
    private int id;

    @NotNull
    @NotBlank
    private String content;

    @NotNull
    private int userId;

    @NotNull
    private int filmId;

    @NotNull
    private int useful;

    @NotNull Optional<Boolean> isPositive;
    @JsonProperty(value="isPositive")
    public boolean isPositive() {
        return isPositive.get();
    }
}