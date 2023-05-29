package ru.yandex.practicum.filmorate.storage.dao.imp;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

@Component
public class FriendDbStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;

    public FriendDbStorage(JdbcTemplate jdbcTemplate, UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
    }

    public void addToFriends(Integer userId, Integer friendId) {
        String sqlQuery = "INSERT INTO friends(user_one_id, user_two_id, status) values (?, ?, ?)";
        jdbcTemplate.update(sqlQuery,userId, friendId, true);
    }

    public void delToFriends(Integer userId, Integer friendId) {
        String sqlQuery = "DELETE FROM friends WHERE user_one_id = ? AND user_two_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    public List<User> findFriendsById(int userId) {
        String sqlQuery = "SELECT DISTINCT u.id, u.email, u.login, u.name, u.birthday " +
                "FROM FRIENDS f " +
                "JOIN users AS u ON f.user_two_id = u.id " +
                "WHERE f.user_one_id = ? AND STATUS = ? " +
                "ORDER BY id";
        return jdbcTemplate.query(sqlQuery, userDbStorage.userRowMapper(),userId, true);
    }

    public List<User> findOtherFriendsById(int userId, int otherId) {
        String sqlQuery = "select * " +
                "from (SELECT DISTINCT u.id, u.email, u.login, u.name, u.birthday " +
                "FROM FRIENDS f " +
                "JOIN users AS u ON f.user_two_id = u.id " +
                "WHERE f.user_one_id = ? AND STATUS = ? " +
                "ORDER BY id) AS u1 " +
                "JOIN " +
                "(SELECT DISTINCT u.id, u.email, u.login, u.name, u.birthday " +
                "FROM FRIENDS f " +
                "JOIN users AS u ON f.user_two_id = u.id " +
                "WHERE f.user_one_id = ? AND STATUS = ? " +
                "ORDER BY id) AS u2 " +
                "ON u1.id = u2.id";
        return jdbcTemplate.query(sqlQuery, userDbStorage.userRowMapper(),userId, true, otherId, true);
    }
}