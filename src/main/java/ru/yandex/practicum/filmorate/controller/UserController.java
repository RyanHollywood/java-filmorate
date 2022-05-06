package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    //Хранилище пользователей
    Map<Integer, User> userStorage = new HashMap<>();

    //Создание пользователя
    @PostMapping
    public User create(@RequestBody User user) {
        userStorage.put(user.getId(), user);
        return user;
    }

    //Обновление пользователя
    @PutMapping
    public User update(@RequestBody User user) {
        userStorage.put(user.getId(), user);
        return user;
    }

    //Получние списка всех пользователей
    @GetMapping
    public List<User> userList() {
        return new ArrayList<>(userStorage.values());
    }

    private boolean filmValidation(User user) {
        return true;
    }
}
