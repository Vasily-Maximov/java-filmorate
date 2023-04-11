package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        User user = userStorage.findById(userId);
        List<User> users = new ArrayList<>();
        for(Long value: user.getFriends()) {
            users.add(userStorage.findById(value.intValue()));
        }
        return users;
    }

    public List<User> findOtherFriendsById(Integer userId, Integer otherId) {
        List<User> mutualFriends = new ArrayList<>();
        User userOne = userStorage.findById(userId);
        User userTwo = userStorage.findById(otherId);
        Set<Long> friendsUserOne = new HashSet<>(userOne.getFriends());
        Set<Long> friendsUserTwo = userTwo.getFriends();
        friendsUserOne.retainAll(friendsUserTwo);
        for (Long friend : friendsUserOne) {
            mutualFriends.add(userStorage.findById(friend.intValue()));
        }
        return mutualFriends;
    }
}