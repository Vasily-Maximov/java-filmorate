package ru.yandex.practicum.filmorate.service;

public class UserValidationException extends RuntimeException {
    public UserValidationException(String message) {
        super(message);
    }
}