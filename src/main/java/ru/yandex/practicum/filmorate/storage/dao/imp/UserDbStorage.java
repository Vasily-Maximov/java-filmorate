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

    protected RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> new User(rs.getInt("id"), rs.getString("email"), rs.getString("login"),
                rs.getString("name"), rs.getDate("birthday").toLocalDate());
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

    @Override
    public User findById(Integer id) {
        String sqlQuery = "SELECT u.* FROM users AS u WHERE u.id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, userRowMapper(), id);
        } catch (DataAccessException e) {
            throw new ObjectNotFoundException(String.format(MessageStatus.PUT_USER_ERROR.getNameStatus(), id));
        }
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "SELECT u.* FROM users AS u ORDER BY u.id";
        return jdbcTemplate.query(sqlQuery, userRowMapper());
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
}