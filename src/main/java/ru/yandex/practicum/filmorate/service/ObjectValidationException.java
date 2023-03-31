package ru.yandex.practicum.filmorate.service;

public class ObjectValidationException extends RuntimeException {
    public ObjectValidationException(String message) {
        super(message);
    }
}