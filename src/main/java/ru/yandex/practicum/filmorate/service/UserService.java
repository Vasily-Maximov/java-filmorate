package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.AbstractDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.imp.UserDbStorage;
import java.util.List;

@Service
public class UserService extends AbstractService<User> {

    private final UserDbStorage userDbStorage;

    @Autowired
    public UserService(AbstractDbStorage<User> userStorage, UserDbStorage userDbStorage) {
        super(userStorage);
        this.userDbStorage = userDbStorage;
    }

    public void addToFriends(Integer userId, Integer friendId) {
        User user = super.findById(userId);
        super.findById(friendId);
        userDbStorage.addToFriends(userId, friendId);
        user.getFriends().add(friendId.longValue());
    }

    public void delToFriends(Integer userId, Integer friendId) {
        User user = super.findById(userId);
        super.findById(friendId);
        userDbStorage.delToFriends(userId, friendId);
        user.getFriends().remove(friendId.longValue());
    }

    public List<User> findFriendsById(Integer userId) {
        return userDbStorage.findFriendsById(userId);
    }

    public List<User> findOtherFriendsById(Integer userId, Integer otherId) {
        return userDbStorage.findOtherFriendsById(userId, otherId);
    }
}