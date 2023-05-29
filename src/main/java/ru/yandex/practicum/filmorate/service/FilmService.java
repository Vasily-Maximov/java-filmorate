package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ObjectValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MessageStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.AbstractDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.imp.LikeDbStorage;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class FilmService extends AbstractService<Film> {

    private static final LocalDate BEGIN_DATE = LocalDate.of(1895,12,28);
    private final AbstractDbStorage<User> userStorage;
    private final LikeDbStorage likeStorage;

    @Autowired
    public FilmService(AbstractDbStorage<Film> filmStorage, AbstractDbStorage<User> userStorage, LikeDbStorage likeStorage) {
        super(filmStorage);
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
    }

    public void addLike(Integer filmId, Integer userId) {
        super.findById(filmId);
        userStorage.findById(userId);
        likeStorage.addLike(filmId, userId);
    }

    public void delLike(Integer filmId, Integer userId) {
        super.findById(filmId);
        userStorage.findById(userId);
        likeStorage.delLike(filmId, userId);
    }

    public List<Film> findPopularFilms(Integer count) {
        return likeStorage.findPopularFilms(count);
    }

    public void create(Film film) {
        checkReleaseDate(film.getName(), film.getReleaseDate());
        super.create(film);
    }

    public void update(Film film) {
        checkReleaseDate(film.getName(), film.getReleaseDate());
        super.update(film);
    }

    private void checkReleaseDate(String name, LocalDate releaseDate) {
        if (!BEGIN_DATE.isBefore(releaseDate)) {
            String messageError = String.format(MessageStatus.ERROR_DATE.getNameStatus(), name);
            log.error(messageError);
            throw new ObjectValidationException(messageError);
        }
    }
}