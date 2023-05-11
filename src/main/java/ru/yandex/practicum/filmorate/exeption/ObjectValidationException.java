package ru.yandex.practicum.filmorate.exeption;

public class ObjectValidationException extends RuntimeException {
    public ObjectValidationException(String message) {
        super(message);
    }
}