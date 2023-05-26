package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.CreateGroup;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MessageStatus;
import ru.yandex.practicum.filmorate.model.UpdateGroup;
import ru.yandex.practicum.filmorate.service.GenreService;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<Genre> getAllGenres() {
        log.info(String.format(MessageStatus.GET_ALL_GENRE.getNameStatus()));
        return genreService.getAll();
    }

    @GetMapping("/{id}")
    public Genre findById(@PathVariable(value = "id") Integer idGenre) {
        log.info(String.format(MessageStatus.GET_GENRE_BY_ID.getNameStatus(),idGenre));
        return genreService.findById(idGenre);
    }

    @PostMapping
    public Genre createGenre(@Validated(CreateGroup.class) @RequestBody Genre genre) {
        log.info(String.format(MessageStatus.POST_GENRE.getNameStatus(), genre.getName()));
        genreService.create(genre);
        return genre;
    }

    @DeleteMapping("/{id}")
    public void delById(@PathVariable(value = "id") Integer idGenre) {
        log.info(String.format(MessageStatus.DELETE_GENRE.getNameStatus(), idGenre));
        genreService.delete(idGenre);
    }

    @PutMapping
    public Genre updateGenre(@Validated(UpdateGroup.class) @RequestBody Genre genre) {
        genreService.findById(genre.getId());
        log.info(String.format(MessageStatus.PUT_GENRE.getNameStatus(), genre.getName()));
        genreService.update(genre);
        return genre;
    }
}