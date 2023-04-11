package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.MessageStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private static Integer globalId = 0;

    private static Integer getNextId() {
        return ++globalId;
    }

    @Override
    public void create(User user) {
        log.info(String.format(MessageStatus.POST_USER.getNameStatus(), user.getName()));
        user.setId(getNextId());
        users.put(user.getId(), user);
    }

    @Override
    public void delete(User user) {
        log.info(String.format(MessageStatus.DELETE_USER.getNameStatus(), user.getName()));
        users.remove(user.getId());
    }

    @Override
    public void update(User user) {
        if (users.get(user.getId()) != null) {
            log.info(String.format(MessageStatus.PUT_USER.getNameStatus(), user.getName()));
            users.put(user.getId(), user);
        } else {
            String messageError = String.format(MessageStatus.PUT_USER_ERROR.getNameStatus(), user.getId());
            log.error(messageError);
            throw new ObjectNotFoundException(messageError);
        }
    }

    @Override
    public User findById(Integer id) {
        return users.values().stream().filter(user -> user.getId().equals(id)).findFirst()
                .orElseThrow(() -> new ObjectNotFoundException(String.format(MessageStatus.PUT_USER_ERROR.getNameStatus(), id)));
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }
}