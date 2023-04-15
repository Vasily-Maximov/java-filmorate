package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.AbstractModel;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

import java.util.List;

public abstract class AbstractService<T extends AbstractModel> {

    private final AbstractStorage<T> storage;

    public AbstractService(AbstractStorage<T> storage) {
        this.storage = storage;
    }

    public void create(T variable) {
        storage.create(variable);
    }

    public void update(T variable, String message) {
        storage.update(variable, message);
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