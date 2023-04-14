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
import ru.yandex.practicum.filmorate.model.MessageStatus;
import ru.yandex.practicum.filmorate.service.UserService;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@Validated(CreateGroup.class) @RequestBody User user) {
        userService.getUserStorage().create(user);
        return user;
    }

    @PutMapping
    public User updateUser(@Validated(UpdateGroup.class) @RequestBody User user) {
        userService.getUserStorage().update(user);
        return userService.getUserStorage().findById(user.getId());
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getUserStorage().getAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable(value = "id") Integer idUser) {
        return userService.getUserStorage().findById(idUser);
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
        log.info(String.format(MessageStatus.ERROR_PARAMETER.getNameStatus(), e.getMessage()));
        return new ErrorResponse(String.format(MessageStatus.ERROR_PARAMETER.getNameStatus(), e.getMessage()));
    }
}