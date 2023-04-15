package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService extends AbstractService<User> {

    @Autowired
    public UserService(AbstractStorage<User> userStorage) {
        super(userStorage);
    }

    public void addToFriends(Integer userId, Integer friendId) {
        User user = super.findById(userId);
        User friend = super.findById(friendId);
        user.getFriends().add(friendId.longValue());
        friend.getFriends().add(userId.longValue());
    }

    public void delToFriends(Integer userId, Integer friendId) {
        User user = super.findById(userId);
        User friend = super.findById(friendId);
        user.getFriends().remove(friendId.longValue());
        friend.getFriends().remove(userId.longValue());
    }

    public List<User> findFriendsById(Integer userId) {
        return super.findById(userId).getFriends().stream().map(aLong -> super.findById(aLong.intValue()))
                .collect(Collectors.toList());
    }

    public List<User> findOtherFriendsById(Integer userId, Integer otherId) {
        return  findFriendsById(userId).stream().filter(findFriendsById(otherId)::contains).collect(Collectors.toList());
    }
}