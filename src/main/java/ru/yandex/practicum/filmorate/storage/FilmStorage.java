package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    void create(Film film);

    void delete(Film film);

    void update(Film film);

    Film findById(Integer id);

    List<Film> getAll();

}