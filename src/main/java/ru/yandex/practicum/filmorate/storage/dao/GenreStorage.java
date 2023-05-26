package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Genre;
import java.util.List;

public interface GenreStorage {

    Genre findById(Integer id);

    List<Genre> getAll();
}