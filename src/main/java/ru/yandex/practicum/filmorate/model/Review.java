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
    int id;

    @NotNull
    @NotBlank
    String content;

    @NotNull
    int userId;

    @NotNull
    int filmId;

    @NotNull
    int useful;

    @NotNull Optional<Boolean> isPositive;
    @JsonProperty(value="isPositive")
    public boolean isPositive() {
        return isPositive.get();
    }
}