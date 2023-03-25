package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.filmorate.model.CreateGroup;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.UpdateGroup;
import ru.yandex.practicum.filmorate.service.FilmValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private Map<Integer,Film> films = new HashMap<>();
    private int generatorId = 1;
    private static final LocalDate BEGIN_DATE = LocalDate.of(1895,12,28);

    @PostMapping
    public Film createFilm(@Validated(CreateGroup.class) @RequestBody Film film) {
        if (BEGIN_DATE.isBefore(film.getReleaseDate())) {
            film.setId(generatorId++);
            films.put(film.getId(), film);
            log.info("Фильм {} добавлен", film.getName());
            return film;
        } else {
            log.error("Дата релиза фильма: {} раньше 28 декабря 1985 года", film.getName());
            throw new FilmValidationException("Дата релиза фильма: " + film.getName() + " раньше 28 декабря 1985 года");
        }
    }

    @PutMapping
    public Film updateFilm(@Validated(UpdateGroup.class) @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            if (BEGIN_DATE.isBefore(film.getReleaseDate())) {
                log.info("Фильм {}  - изменён", film.getName());
                films.put(film.getId(), film);
                return films.get(film.getId());
            } else {
                log.error("Дата релиза фильма: {} раньше 28 декабря 1985 года", film.getName());
                throw new FilmValidationException("Дата релиза фильма: " + film.getName() + " раньше 28 декабря 1985 года");
            }
        } else {
            log.error("Фильм с id = {} не найден", film.getId());
            throw new FilmValidationException("Фильм с id = " + film.getId() + " не найден");
        }
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }
}