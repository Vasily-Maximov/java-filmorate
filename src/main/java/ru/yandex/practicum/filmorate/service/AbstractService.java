package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.AbstractModel;
import ru.yandex.practicum.filmorate.storage.dao.AbstractDbStorage;
import java.util.List;

public abstract class AbstractService<T extends AbstractModel> {

    protected final AbstractDbStorage<T> storage;

    public AbstractService(AbstractDbStorage<T> storage) {
        this.storage = storage;
    }

    public void create(T variable) {
        storage.create(variable);
    }

    public void update(T variable) {
        findById(variable.getId());
        storage.update(variable);
    }

    public T findById(Integer id) {
        return storage.findById(id);
    }

    public List<T> getAll() {
        return storage.getAll();
    }

    public void delete(Integer id) {
        storage.delete(storage.findById(id));
    }
}