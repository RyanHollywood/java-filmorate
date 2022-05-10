package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    Map<Integer, User> userStorage = new HashMap<>();

    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug(user.getLogin() + " added to userStorage");
        userStorage.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.debug(user.getLogin() + " updated in userStorage");
        userStorage.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public List<User> userList() {
        return new ArrayList<>(userStorage.values());
    }
}
