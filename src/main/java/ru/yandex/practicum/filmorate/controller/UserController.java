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
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends Controllers<User> {

    @PostMapping
    public User createUser(@Validated(CreateGroup.class) @RequestBody User user) {
        return super.create(user);
    }

    @PutMapping
    public User updateUser(@Validated(UpdateGroup.class) @RequestBody User user) throws UserValidationException {
        return super.update(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return super.getAll();
    }

}