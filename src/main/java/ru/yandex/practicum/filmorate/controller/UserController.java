package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.model.CreateGroup;
import ru.yandex.practicum.filmorate.model.ErrorResponse;
import ru.yandex.practicum.filmorate.model.UpdateGroup;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserStorage userStorage;

    @Autowired
    public UserController(UserService userService, UserStorage userStorage) {
        this.userService = userService;
        this.userStorage = userStorage;
    }

    @PostMapping
    public User createUser(@Validated(CreateGroup.class) @RequestBody User user) {
        userStorage.create(user);
        return user;
    }

    @PutMapping
    public User updateUser(@Validated(UpdateGroup.class) @RequestBody User user) {
        userStorage.update(user);
        return userStorage.findById(user.getId());
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userStorage.getAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable(value = "id") Integer idUser) {
        return userStorage.findById(idUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriends(@PathVariable(value = "id") Integer userId, @PathVariable Integer friendId) {
        userService.addToFriends(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void delToFriends(@PathVariable(value = "id") Integer userId, @PathVariable Integer friendId) {
        userService.delToFriends(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> findFriendsById(@PathVariable(value = "id") Integer userId) {
        return userService.findFriendsById(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findOtherFriendsById(@PathVariable(value = "id") Integer userId, @PathVariable Integer otherId) {
        return userService.findOtherFriendsById(userId, otherId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectParameter(final NumberFormatException e) {
        return new ErrorResponse(String.format("Некорректно передан параметр: %s", e.getMessage()));
    }
}