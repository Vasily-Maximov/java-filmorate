package ru.yandex.practicum.filmorate.storage.dao.imp;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MessageStatus;
import ru.yandex.practicum.filmorate.storage.dao.AbstractDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.GenreStorage;
import java.sql.PreparedStatement;
import java.util.List;

@Component
public class GenreDbStorage extends AbstractDbStorage<Genre> implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> new Genre(rs.getInt("id"), rs.getString("name"));
    }

    private int saveAndReturnId(Genre genre) {
        String sqlQuery = "INSERT INTO genres(name) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, genre.getName());
            return stmt;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public Genre findById(Integer id) {
        String sqlQuery = "SELECT * FROM genres WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, genreRowMapper(), id);
        } catch (DataAccessException e) {
            throw new ObjectNotFoundException(String.format(MessageStatus.GET_GENRE_ERROR.getNameStatus(), id));
        }
    }

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "SELECT * FROM genres ORDER BY id";
        return jdbcTemplate.query(sqlQuery, genreRowMapper());
    }

    @Override
    public void create(Genre genre) {
        genre.setId(saveAndReturnId(genre));
    }

    @Override
    public void delete(Genre genre) {
        String sqlQuery = "DELETE FROM genres WHERE id = ?";
        jdbcTemplate.update(sqlQuery, genre.getId());
    }

    @Override
    public void update(Genre genre) {
        String sqlQuery = "UPDATE genres SET name = ? WHERE id = ?";
        jdbcTemplate.update(sqlQuery, genre.getName(), genre.getId());
    }
}