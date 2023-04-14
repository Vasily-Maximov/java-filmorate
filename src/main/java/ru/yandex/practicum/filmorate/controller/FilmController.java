package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.filmorate.model.CreateGroup;
import ru.yandex.practicum.filmorate.model.ErrorResponse;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.UpdateGroup;
import ru.yandex.practicum.filmorate.model.MessageStatus;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film createFilm(@Validated(CreateGroup.class) @RequestBody Film film) {
        filmService.getFilmStorage().create(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Validated(UpdateGroup.class) @RequestBody Film film) {
        filmService.getFilmStorage().update(film);
        return filmService.getFilmStorage().findById(film.getId());
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getFilmStorage().getAll();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable(value = "id") Integer idFilm) {
        return filmService.getFilmStorage().findById(idFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable(value = "id") Integer filmId, @PathVariable Integer userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void delLike(@PathVariable(value = "id") Integer filmId, @PathVariable Integer userId) {
        filmService.delLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> findPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.findPopularFilms(count);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectParameter(final NumberFormatException e) {
        log.info(String.format(MessageStatus.ERROR_PARAMETER.getNameStatus(), e.getMessage()));
        return new ErrorResponse(String.format(MessageStatus.ERROR_PARAMETER.getNameStatus(), e.getMessage()));
    }
}