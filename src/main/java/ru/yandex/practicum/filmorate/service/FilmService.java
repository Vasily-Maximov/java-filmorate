package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Integer filmId, Integer userId) {
        Film film = filmStorage.findById(filmId);
        film.getLikes().add(userId.longValue());
        filmStorage.update(film);
    }

    public void delLike(Integer filmId, Integer userId) {
        Film film = filmStorage.findById(filmId);
        User user = userStorage.findById(userId);
        film.getLikes().remove(user.getId().longValue());
        filmStorage.update(film);
    }

    public List<Film> findPopularFilms(Integer count) {
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparingInt(f -> -1 * f.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}