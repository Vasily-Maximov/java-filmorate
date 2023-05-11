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

    public void create(Film film) {
        super.create(film);
    }

    public void delete(Film film) {
        super.delete(film);
    }

    public void update(Film film) {
        super.update(film, MessageStatus.PUT_FILM_ERROR.getNameStatus());
    }

    public Film findById(Integer id) {
        Film film = super.findById(id);
        if (film != null) {
            return film;
        } else {
            throw new ObjectNotFoundException(String.format(MessageStatus.PUT_FILM_ERROR.getNameStatus(), id));
        }
    }

    public List<Film> getAll() {
        return new ArrayList<>(super.getAll());
    }
}