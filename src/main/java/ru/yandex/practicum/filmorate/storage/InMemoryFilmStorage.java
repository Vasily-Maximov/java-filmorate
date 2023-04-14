package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MessageStatus;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class InMemoryFilmStorage extends AbstractStorage<Film> implements FilmStorage {

    @Override
    public void create(Film film) {
        log.info(String.format(MessageStatus.POST_FILM.getNameStatus(), film.getName()));
        super.create(film);
    }

    @Override
    public void delete(Film film) {
        log.info(String.format(MessageStatus.DELETE_FILM.getNameStatus(), film.getName()));
        super.delete(film);
    }

    @Override
    public void update(Film film) {
        log.info(String.format(MessageStatus.PUT_FILM.getNameStatus(), film.getName()));
        super.update(film, MessageStatus.PUT_FILM_ERROR.getNameStatus());
    }

    @Override
    public Film findById(Integer id) {
        return super.findByIdModel(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format(MessageStatus.PUT_FILM_ERROR.getNameStatus(), id)));
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(super.getAll());
    }
}