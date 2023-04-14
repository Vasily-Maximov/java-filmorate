package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addToFriends(Integer userId, Integer friendId) {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);
        user.getFriends().add(friendId.longValue());
        userStorage.update(userStorage.findById(userId));
        friend.getFriends().add(userId.longValue());
        userStorage.update(userStorage.findById(friendId));
    }

    public void delToFriends(Integer userId, Integer friendId) {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);
        user.getFriends().remove(friendId.longValue());
        userStorage.update(user);
        friend.getFriends().remove(userId.longValue());
        userStorage.update(friend);
    }

    public List<User> findFriendsById(Integer userId) {
        return userStorage.findById(userId).getFriends().stream().map(aLong -> userStorage.findById(aLong.intValue()))
                .collect(Collectors.toList());
    }

    public List<User> findOtherFriendsById(Integer userId, Integer otherId) {
        return  findFriendsById(userId).stream().filter(findFriendsById(otherId)::contains).collect(Collectors.toList());
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }
}