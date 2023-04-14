package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.model.ErrorResponse;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exeption.ObjectValidationException;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleObjectNotFoundException(final ObjectNotFoundException e) {
        log.info(String.format("error: %s", e.getMessage()));
        return new ErrorResponse(String.format("error: %s", e.getMessage()));
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleObjectValidationException(final ObjectValidationException e) {
        log.info(String.format("error: %s", e.getMessage()));
        return new ErrorResponse(String.format("error: %s", e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.info(String.format("Поле:= %s - %s", Objects.requireNonNull(e.getFieldError()).getField(),
                e.getFieldError().getDefaultMessage()));
        return new ErrorResponse(String.format("Поле:= %s - %s", Objects.requireNonNull(e.getFieldError()).getField(),
                e.getFieldError().getDefaultMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.info("Произошла непредвиденная ошибка.");
        return new ErrorResponse("Произошла непредвиденная ошибка.");
    }
}