package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.AbstractDbStorage;

@Service
public class GenreService extends AbstractService<Genre> {

    @Autowired
    public GenreService(AbstractDbStorage<Genre> genreStorage) {
        super(genreStorage);
    }
}