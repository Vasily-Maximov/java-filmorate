package ru.yandex.practicum.filmorate.model;

public enum MessageStatus {
    POST_FILM("Фильм %s добавлен"),
    PUT_FILM("Фильм %s - изменён"),
    PUT_FILM_ERROR("Фильм с id = %d не найден"),
    DELETE_FILM("Фильм %s удалён"),
    POST_USER("Пользователь %s добавлен"),
    DELETE_USER("Пользователь %s удалён"),
    PUT_USER("Пользователь %s - изменён"),
    PUT_USER_ERROR("Пользователь с id = %d не найден"),
    ERROR_DATE("Дата релиза фильма: %s раньше 28 декабря 1895 года"),
    ERROR_LOGIN("Логин := %s содержит пробел");
    private final String nameStatus;

    MessageStatus(String nameStatus) {
        this.nameStatus = nameStatus;
    }

    public String getNameStatus() {
        return nameStatus;
    }
}
