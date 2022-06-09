package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Data
public class User {
    @NotNull
    private long id;

    @NotNull
    @NotBlank
    @Email
    private String email;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^\\S*$")
    private String login;

    private String name;

    @NotNull
    @Past
    private LocalDate birthday;

    private Set<Long> friends;

    public User(long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        checkAndReplaceName(name);
        this.birthday = birthday;
        friends = new HashSet<>();
    }

    public void setName(String name) {
        checkAndReplaceName(name);
    }

    public void addFriend(Long friendId) {
        friends.add(friendId);
    }

    public void deleteFriend(Long friendId) {
        friends.remove(friendId);
    }

    private void checkAndReplaceName(String name) {
        if (Optional.ofNullable(name).isEmpty() || name.isBlank()) {
            this.name = login;
        } else {
            this.name = name;
        }
    }
}
