package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.AbstractDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.imp.LikeDbStorage;
import java.util.List;

@Service
public class FilmService extends AbstractService<Film> {
    private final AbstractDbStorage<User> userStorage;
    private final LikeDbStorage likeStorage;

    @Autowired
    public FilmService(AbstractDbStorage<Film> filmStorage, AbstractDbStorage<User> userStorage, LikeDbStorage likeStorage) {
        super(filmStorage);
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
    }

    public void addLike(Integer filmId, Integer userId) {
        Film film = super.findById(filmId);
        userStorage.findById(userId);
        likeStorage.delLike(filmId, userId);
        likeStorage.addLike(filmId, userId);
        film.getLikes().add(userId.longValue());
    }

    public void delLike(Integer filmId, Integer userId) {
        Film film = super.findById(filmId);
        userStorage.findById(userId);
        likeStorage.delLike(filmId, userId);
        film.getLikes().remove(userId.longValue());
    }

    public List<Film> findPopularFilms(Integer count) {
        return likeStorage.findPopularFilms(count);
    }
}