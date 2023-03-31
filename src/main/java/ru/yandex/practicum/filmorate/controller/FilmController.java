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
import ru.yandex.practicum.filmorate.model.MessageStatus;
import ru.yandex.practicum.filmorate.model.UpdateGroup;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends Controllers<Film> {

    private int id = 0;
    @PostMapping
    public Film createFilm(@Validated(CreateGroup.class) @RequestBody Film film) {
        film.setId(++id);
        log.info(String.format(MessageStatus.POST_FILM.getNameStatus(), film.getName()));
        return super.create(film, film.getId());
    }

    @PutMapping
    public Film updateFilm(@Validated(UpdateGroup.class) @RequestBody Film film) {
        return super.update(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return super.getAll();
    }
}