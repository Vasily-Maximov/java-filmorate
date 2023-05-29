package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ObjectValidationException;
import ru.yandex.practicum.filmorate.model.MessageStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.AbstractDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.imp.FriendDbStorage;
import java.util.List;

@Service
@Slf4j
public class UserService extends AbstractService<User> {

    private final FriendDbStorage friendDbStorage;

    @Autowired
    public UserService(AbstractDbStorage<User> userStorage, FriendDbStorage friendDbStorage) {
        super(userStorage);
        this.friendDbStorage = friendDbStorage;
    }

    public void addToFriends(Integer userId, Integer friendId) {
        super.findById(userId);
        super.findById(friendId);
        friendDbStorage.addToFriends(userId, friendId);
    }

    public void delToFriends(Integer userId, Integer friendId) {
        super.findById(userId);
        super.findById(friendId);
        friendDbStorage.delToFriends(userId, friendId);
    }

    public List<User> findFriendsById(Integer userId) {
        return friendDbStorage.findFriendsById(userId);
    }

    public List<User> findOtherFriendsById(Integer userId, Integer otherId) {
        return friendDbStorage.findOtherFriendsById(userId, otherId);
    }

    public void create(User user) {
        checkLogin(user.getLogin());
        checkName(user);
        super.create(user);
    }

    public void update(User user) {
        checkLogin(user.getLogin());
        checkName(user);
        super.update(user);
    }

    private void checkLogin(String login) {
        if (login.trim().contains(" ")) {
            String messageError = String.format(MessageStatus.ERROR_LOGIN.getNameStatus(), login);
            log.error(messageError);
            throw new ObjectValidationException(messageError);
        }
    }

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}