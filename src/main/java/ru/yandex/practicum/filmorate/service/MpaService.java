package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.AbstractDbStorage;

@Service
public class MpaService extends AbstractService<Mpa> {

    @Autowired
    public MpaService(AbstractDbStorage<Mpa> mpaDbStorage) {
        super(mpaDbStorage);
    }
}