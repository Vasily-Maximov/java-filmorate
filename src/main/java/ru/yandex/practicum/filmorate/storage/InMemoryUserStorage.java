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

    @Override
    public void create(User user) {
        log.info(String.format(MessageStatus.POST_USER.getNameStatus(), user.getName()));
        super.create(user);
    }

    @Override
    public void delete(User user) {
        log.info(String.format(MessageStatus.DELETE_USER.getNameStatus(), user.getName()));
        super.delete(user);
    }

    @Override
    public void update(User user) {
        log.info(String.format(MessageStatus.PUT_USER.getNameStatus(), user.getName()));
        super.update(user, MessageStatus.PUT_USER_ERROR.getNameStatus());
    }

    @Override
    public User findById(Integer id) {
        return super.findByIdModel(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format(MessageStatus.PUT_USER_ERROR.getNameStatus(), id)));
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(super.getAll());
    }
}