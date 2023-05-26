package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Mpa;
import java.util.List;

public interface MpaStorage {

    Mpa findById(Integer id);

    List<Mpa> getAll();
}