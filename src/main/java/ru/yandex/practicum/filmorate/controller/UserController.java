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
        log.info(String.format(MessageStatus.POST_USER.getNameStatus(), user.getName()));
        userService.create(user);
        return user;
    }

    @PutMapping
    public User updateUser(@Validated(UpdateGroup.class) @RequestBody User user) {
        log.info(String.format(MessageStatus.PUT_USER.getNameStatus(), user.getName()));
        userService.update(user, MessageStatus.PUT_USER_ERROR.getNameStatus());
        return userService.findById(user.getId());
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info(MessageStatus.GET_ALL_USER.getNameStatus());
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable(value = "id") Integer idUser) {
        log.info(String.format(MessageStatus.GET_USER_BY_ID.getNameStatus(), idUser));
        return userService.findById(idUser);
    }

    @DeleteMapping("/{id}")
    public void delById(@PathVariable(value = "id") Integer idUser) {
        log.info(String.format(MessageStatus.DELETE_USER.getNameStatus(), idUser));
        userService.delete(idUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriends(@PathVariable(value = "id") Integer userId, @PathVariable Integer friendId) {
        log.info(String.format(MessageStatus.PUT_USER_FRIEND.getNameStatus(), userId, friendId));
        userService.addToFriends(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void delToFriends(@PathVariable(value = "id") Integer userId, @PathVariable Integer friendId) {
        log.info(String.format(MessageStatus.DELETE_USER_FRIEND.getNameStatus(), userId, friendId));
        userService.delToFriends(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> findFriendsById(@PathVariable(value = "id") Integer userId) {
        log.info(String.format(MessageStatus.GET_USER_FRIEND.getNameStatus(), userId));
        return userService.findFriendsById(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findOtherFriendsById(@PathVariable(value = "id") Integer userId, @PathVariable Integer otherId) {
        log.info(String.format(MessageStatus.GET_FRIENDS.getNameStatus(), userId, otherId));
        return userService.findOtherFriendsById(userId, otherId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectParameter(final NumberFormatException e) {
        log.info(String.format(MessageStatus.ERROR_PARAMETER.getNameStatus(), e.getMessage()));
        return new ErrorResponse(String.format(MessageStatus.ERROR_PARAMETER.getNameStatus(), e.getMessage()));
    }
}