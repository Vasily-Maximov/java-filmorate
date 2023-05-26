package ru.yandex.practicum.filmorate.storage.dao.imp;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.MessageStatus;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.AbstractDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.MpaStorage;
import java.sql.PreparedStatement;
import java.util.List;

@Component
public class MpaDbStorage extends AbstractDbStorage<Mpa> implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Mpa> mpaRowMapper() {
        return (rs, rowNum) -> new Mpa(rs.getInt("id"), rs.getString("name"));
    }

    private int saveAndReturnId(Mpa mpa) {
        String sqlQuery = "INSERT INTO mpa(name) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, mpa.getName());
            return stmt;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public Mpa findById(Integer id) {
        String sqlQuery = "SELECT * FROM mpa WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, mpaRowMapper(), id);
        } catch (DataAccessException e) {
            throw new ObjectNotFoundException(String.format(MessageStatus.GET_MPA_ERROR.getNameStatus(), id));
        }
    }

    @Override
    public List<Mpa> getAll() {
        String sqlQuery = "SELECT * FROM mpa ORDER BY id";
        return jdbcTemplate.query(sqlQuery, mpaRowMapper());
    }

    @Override
    public void create(Mpa mpa) {
        mpa.setId(saveAndReturnId(mpa));
    }

    @Override
    public void delete(Mpa mpa) {
        String sqlQuery = "DELETE FROM mpa WHERE id = ?";
        jdbcTemplate.update(sqlQuery, mpa.getId());
    }

    @Override
    public void update(Mpa mpa) {
        String sqlQuery = "UPDATE mpa SET name = ? WHERE id = ?";
        jdbcTemplate.update(sqlQuery, mpa.getName(), mpa.getId());
    }
}