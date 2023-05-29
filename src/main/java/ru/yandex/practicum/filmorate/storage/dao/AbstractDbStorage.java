package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.AbstractModel;
import java.util.List;

@Slf4j
public abstract class AbstractDbStorage<T extends AbstractModel> {

    public abstract void create(T variable);

    public abstract void delete(T variable);

    public abstract void update(T variable);

    public abstract T findById(Integer id);

    public abstract List<T> getAll();
}