package ru.yandex.practicum.filmorate.storage.dao.imp;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.MessageStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dao.AbstractDbStorage;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Component
public class UserDbStorage extends AbstractDbStorage<User> implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<User> usersRowMapper() {
        return (rs, rowNum) -> UserDbStorage.this.findById(rs.getInt("id"));
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User user = new User(rs.getInt("id"), rs.getString("email"), rs.getString("login"),
                    rs.getString("name"), rs.getDate("birthday").toLocalDate());
            do {
                if (rs.getBoolean("status")) {
                    user.getFriends().add(rs.getLong("id_friend"));
                }
            } while (rs.next());
            return user;
        };
    }

    private int saveAndReturnId(User user) {
        String sqlQuery = "INSERT INTO users(email, login, name, birthday) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    private void delLink(User user) {
        delLinkUserFriends(user.getId());
        delLinkUserLike(user.getId());
    }

    private void delLinkUserFriends(int id) {
        String sqlQuery = "DELETE FROM friends WHERE user_one_id = ? OR user_two_id = ?";
        jdbcTemplate.update(sqlQuery, id, id);
    }

    private void delLinkUserLike(int id) {
        String sqlQuery = "DELETE FROM likes WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    public List<User> findFriendsById(int userId) {
        String sqlQuery = "SELECT DISTINCT u.id, u.email, u.login, u.name, u.birthday " +
                "FROM FRIENDS f " +
                "JOIN users AS u ON f.user_two_id = u.id " +
                "WHERE f.user_one_id = ? AND STATUS = ? " +
                "ORDER BY id";
        return jdbcTemplate.query(sqlQuery, usersRowMapper(),userId, true);
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
        return jdbcTemplate.query(sqlQuery, usersRowMapper(),userId, true, otherId, true);
    }

    @Override
    public User findById(Integer id) {
        String sqlQuery = "SELECT u.*, f.user_two_id AS id_friend, f.status FROM users AS u LEFT JOIN friends AS f ON u.id =" +
                "f.user_one_id WHERE u.id = ? ORDER BY u.id";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, userRowMapper(), id);
        } catch (DataAccessException e) {
            throw new ObjectNotFoundException(String.format(MessageStatus.PUT_USER_ERROR.getNameStatus(), id));
        }
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "SELECT id FROM users ORDER BY id";
        return jdbcTemplate.query(sqlQuery, usersRowMapper());
    }

    @Override
    public void create(User user) {
        user.setId(saveAndReturnId(user));
    }

    @Override
    public void delete(User user) {
        delLink(user);
        String sqlQuery = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sqlQuery, user.getId());
    }

    @Override
    public void update(User user) {
        String sqlQuery = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
    }

    public void addToFriends(Integer userId, Integer friendId) {
        String sqlQuery = "INSERT INTO friends(user_one_id, user_two_id, status) values (?, ?, ?)";
        jdbcTemplate.update(sqlQuery,userId, friendId, true);
    }

    public void delToFriends(Integer userId, Integer friendId) {
        String sqlQuery = "DELETE FROM friends WHERE user_one_id = ? AND user_two_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }
}