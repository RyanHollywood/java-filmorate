package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    @NonNull
    //@Positive
    private final int id;
    @NonNull
    @NotBlank
    @Email
    private String email;
    @NonNull
    @NotBlank
    @Pattern(regexp = "^\\S*$")
    private final String login;
    private String name;
    @Past
    private final LocalDate birthday;
}
