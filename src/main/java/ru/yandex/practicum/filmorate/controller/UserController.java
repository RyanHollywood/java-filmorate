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

    //Хранилище пользователей
    Map<Integer, User> userStorage = new HashMap<>();

    //Логгер
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    //Создание пользователя
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        userStorage.put(user.getId(), user);
        return user;
    }

    //Обновление пользователя - исправить
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        userStorage.put(user.getId(), user);
        return user;
    }

    //Получение списка всех пользователей
    @GetMapping
    public List<User> userList() {
        return new ArrayList<>(userStorage.values());
    }
}
