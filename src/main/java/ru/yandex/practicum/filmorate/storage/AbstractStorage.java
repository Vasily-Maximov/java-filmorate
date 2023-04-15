package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.AbstractModel;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


@Slf4j
public abstract class AbstractStorage<T extends AbstractModel> {

    private final Map<Integer, T> hashMapModel = new HashMap<>();
    private Integer globalId = 0;

    private Integer getNextId() {
        return ++globalId;
    }

    public void create(T variable) {
        variable.setId(getNextId());
        hashMapModel.put(globalId,variable);
    }

    public void delete(T variable) {
        hashMapModel.remove(variable.getId());
    }

    public void update(T variable, String message) {
        if (hashMapModel.get(variable.getId()) != null) {
            hashMapModel.put(variable.getId(), variable);
        } else {
            String messageError = String.format(message, variable.getId());
            log.error(messageError);
            throw new ObjectNotFoundException(messageError);
        }
    }

    public T findById(Integer id) {
        return hashMapModel.get(id);
    }

    public List<T> getAll() {
        return new ArrayList<>(hashMapModel.values());
    }
}