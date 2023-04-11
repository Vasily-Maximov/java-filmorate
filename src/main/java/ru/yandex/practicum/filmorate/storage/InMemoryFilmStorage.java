package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MessageStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private static Integer globalId = 0;

    private static Integer getNextId() {
        return ++globalId;
    }

    @Override
    public void create(Film film) {
        log.info(String.format(MessageStatus.POST_FILM.getNameStatus(), film.getName()));
        film.setId(getNextId());
        films.put(globalId, film);
    }

    @Override
    public void delete(Film film) {
        log.info(String.format(MessageStatus.DELETE_FILM.getNameStatus(), film.getName()));
        films.remove(film.getId());
    }

    @Override
    public void update(Film film) {
        if (films.get(film.getId()) != null) {
            log.info(String.format(MessageStatus.PUT_FILM.getNameStatus(), film.getName()));
            films.put(film.getId(), film);
        } else {
            String messageError = String.format(MessageStatus.PUT_FILM_ERROR.getNameStatus(), film.getId());
            log.error(messageError);
            throw new ObjectNotFoundException(messageError);
        }
    }

    @Override
    public Film findById(Integer id) {
        return films.values().stream().filter(film -> film.getId().equals(id)).findFirst()
                .orElseThrow(() -> new ObjectNotFoundException(String.format(MessageStatus.PUT_FILM_ERROR.getNameStatus(), id)));
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }
}