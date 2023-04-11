package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(Integer filmId, Integer userId) {
        Film film = filmStorage.findById(filmId);
        film.getLikes().add(userId.longValue());
        filmStorage.update(film);
    }

    public void delLike(Integer filmId, Integer userId) {
        Film film = filmStorage.findById(filmId);
        film.getLikes().remove(userId.longValue());
        filmStorage.update(film);
    }

    public List<Film> findPopularFilms(Integer count) {
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparingInt(f -> -1 * f.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

}