package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.MessageStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class InMemoryUserStorage extends AbstractStorage<User> implements UserStorage {

    public void create(User user) {
        super.create(user);
    }

    public void delete(User user) {
        super.delete(user);
    }

    public void update(User user) {
        super.update(user, MessageStatus.PUT_USER_ERROR.getNameStatus());
    }

    public User findById(Integer id) {
        User user = super.findById(id);
        if (user != null) {
            return user;
        } else {
            throw new ObjectNotFoundException(String.format(MessageStatus.PUT_USER_ERROR.getNameStatus(), id));
        }
    }

    public List<User> getAll() {
        return new ArrayList<>(super.getAll());
    }
}