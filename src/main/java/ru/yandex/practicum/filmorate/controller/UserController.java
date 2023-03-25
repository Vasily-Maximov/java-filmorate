package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.filmorate.model.CreateGroup;
import ru.yandex.practicum.filmorate.model.UpdateGroup;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private Map<Integer, User> users = new HashMap<>();
    private int generatorId = 1;

    @PostMapping
    public User createUser(@Validated(CreateGroup.class) @RequestBody User user) {
        if (! user.getLogin().trim().contains(" ")) {
            user.setId(generatorId++);
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            if (user.getName().isBlank()){
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.info("Пользователь {} добавлен", user.getName());
            return user;
        } else {
            log.info("Логин {} содержит пробел", user.getLogin());
            throw new UserValidationException("Логин - " + user.getLogin() + " содержит пробел");
        }
    }

    @PutMapping
    public User updateUser(@Validated(UpdateGroup.class) @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            if (! user.getLogin().trim().contains(" ")) {
                log.info("Пользователь {}  - изменён", user.getName());
                users.put(user.getId(), user);
                return users.get(user.getId());
            } else {
                log.info("Логин {} содержит пробел", user.getLogin());
                throw new UserValidationException("Логин - " + user.getLogin() + " содержит пробел");
            }
        } else {
            log.error("Пользователь с id = {} не найден", user.getId());
            throw new UserValidationException("Пользователь с id = " + user.getId() + " не найден");
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
