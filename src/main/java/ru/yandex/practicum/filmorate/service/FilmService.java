package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService extends AbstractService<Film> {
    private final UserStorage userStorage;

    @Autowired
    public FilmService(AbstractStorage<Film> filmStorage, UserStorage userStorage) {
        super(filmStorage);
        this.userStorage = userStorage;
    }

    public void addLike(Integer filmId, Integer userId) {
        Film film = super.findById(filmId);
        userStorage.findById(userId);
        film.getLikes().add(userId.longValue());
    }

    public void delLike(Integer filmId, Integer userId) {
        Film film = super.findById(filmId);
        userStorage.findById(userId);
        film.getLikes().remove(userId.longValue());
    }

    public List<Film> findPopularFilms(Integer count) {
        return super.getAll().stream()
                .sorted(Comparator.comparingInt(f -> -1 * f.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}