package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    void create(User user);

    void delete(User user);

    void update(User user);

    User findById(Integer id);

    List<User> getAll();
}