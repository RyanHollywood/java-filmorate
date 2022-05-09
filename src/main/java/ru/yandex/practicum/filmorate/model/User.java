package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User {
    private final int id;
    @NonNull
    @Email
    private String email;
    @NonNull
    @NotBlank
    private final String login;
    private String name;
    @Past
    private final LocalDate birthday;
}
