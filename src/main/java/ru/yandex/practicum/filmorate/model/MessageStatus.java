package ru.yandex.practicum.filmorate.model;

public enum MessageStatus {
    POST_FILM("Фильм %s добавлен"),
    PUT_FILM("Фильм %s - изменён"),
    PUT_FILM_ERROR("Фильм с id = %d не найден"),
    GET_ALL_FILMS("Вывод всех фильмов"),
    GET_FILM_BY_ID("Вывод фильма по id = %d"),
    DELETE_FILM("Фильм с id = %d удалён"),
    PUT_LIKE_FILM("Пользователь с id = %d ставит лайк фильму с id = %d"),
    GET_POPULAR_FILM("Возвращаем список из первых count = %d фильмов по количеству лайков"),
    DELETE_LIKE_FILM("Пользователь с id = %d удаляет лайк фильму с id = %d"),
    POST_USER("Пользователь %s добавлен"),
    DELETE_USER("Пользователь с id = %d удалён"),
    PUT_USER("Пользователь %s - изменён"),
    PUT_USER_ERROR("Пользователь с id = %d не найден"),
    GET_ALL_USER("Вывод всех пользователей"),
    GET_USER_BY_ID("Вывод пользователя по id = %d"),
    PUT_USER_FRIEND("Добавление в друзья пользователя с id = %d пользователю с id = %d"),
    DELETE_USER_FRIEND("Удаление из друзей пользователя с id = %d у пользователя с id = %d"),
    GET_USER_FRIEND("Вывод всех друзей пользователя с id = %d"),
    GET_FRIENDS("Вывод общих друзей пользователя с id = %d и id %d"),
    ERROR_DATE("Дата релиза фильма: %s раньше 28 декабря 1895 года"),
    ERROR_LOGIN("Логин := %s содержит пробел"),
    ERROR_PARAMETER("Некорректно передан параметр: %s"),
    GET_ALL_GENRE("Вывод всех жанров"),
    GET_GENRE_BY_ID("Вывод жанра по id = %d"),
    GET_GENRE_ERROR("Жанр с id = %d не найден"),
    POST_GENRE("Жанр %s добавлен"),
    DELETE_GENRE("Жанр с id = %d удалён"),
    PUT_GENRE("Жанр %s - изменён"),
    GET_ALL_MPA("Вывод всех MPA"),
    GET_MPA_BY_ID("Вывод MPA по id = %d"),
    GET_MPA_ERROR("MPA с id = %d не найден"),
    POST_MPA("MPA %s добавлен"),
    DELETE_MPA("MPA с id = %d удалён"),
    PUT_MPA("MPA %s - изменён");

    private final String nameStatus;

    MessageStatus(String nameStatus) {
        this.nameStatus = nameStatus;
    }

    public String getNameStatus() {
        return nameStatus;
    }
}
