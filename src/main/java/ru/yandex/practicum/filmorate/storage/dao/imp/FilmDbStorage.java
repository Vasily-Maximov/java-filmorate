package ru.yandex.practicum.filmorate.storage.dao.imp;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MessageStatus;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dao.AbstractDbStorage;
import java.util.HashSet;
import java.util.List;

@Component
public class FilmDbStorage extends AbstractDbStorage<Film> implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDbStorage mpaDbStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaDbStorage mpaDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDbStorage = mpaDbStorage;
    }

    protected RowMapper<Film> filmRowMapper() {
        return (rs, rowNum) -> {
            Film film = new Film(rs.getInt("id"), rs.getString("name"),
                    rs.getString("description"), rs.getDate("release_date").toLocalDate(),
                    rs.getLong("duration"));
            film.setMpa(new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")));
            return film;
        };
    }

    private int simpleSave(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        return simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();
    }

    protected void setAdditionalMovieFields(Film film) {
        film.getLikes().addAll(findLikesFilm(film.getId()));
        film.setMpa(mpaDbStorage.findById(film.getMpa().getId()));
        film.setGenres(new HashSet<>((findGenresFilm(film.getId()))));
    }

    private List<Long> findLikesFilm(int film_id) {
        String sqlQuery = "SELECT user_id FROM LIKES WHERE film_id = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getLong("user_id"), film_id);
    }

    private List<Genre> findGenresFilm(int film_id) {
        String sqlQuery = "SELECT fg.genre_id AS id, g.name " +
                "FROM film_genres fg " +
                "JOIN genres AS g ON fg.genre_id = g.id " +
                "WHERE film_id = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new Genre(rs.getInt("id"), rs.getString("name")),
                film_id);
    }

    private void createLink(Film film) {
        createLinkFilmGenres(film);
    }

    private void createLinkFilmGenres(Film film) {
        for (Genre genre: film.getGenres()) {
            String sqlQuery = "INSERT INTO film_genres(film_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
        }
    }

    private void delLink(Film film) {
        delLinkFilmGenres(film.getId());
        delLinkFilmLike(film.getId());
    }

    private void delLinkFilmGenres(int id) {
        String sqlQuery = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    private void delLinkFilmLike(int id) {
        String sqlQuery = "DELETE FROM likes WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public Film findById(Integer id) {
        String sqlQuery = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.mpa_id, m.name AS mpa_name " +
                "FROM films AS f " +
                "JOIN mpa AS m ON f.mpa_id = m.id " +
                "WHERE f.id = ?";
        try {
            Film film =  jdbcTemplate.queryForObject(sqlQuery, filmRowMapper(), id);
            setAdditionalMovieFields(film);
            return film;
        } catch (DataAccessException e) {
            throw new ObjectNotFoundException(String.format(MessageStatus.PUT_FILM_ERROR.getNameStatus(), id));
        }
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.mpa_id, m.name AS mpa_name " +
                "FROM films AS f " +
                "JOIN mpa AS m ON f.mpa_id = m.id " +
                "ORDER BY id";
        List<Film> films = jdbcTemplate.query(sqlQuery, filmRowMapper());
        for (Film film: films) {
            setAdditionalMovieFields(film);
        }
        return films;
    }

    @Override
    public void create(Film film) {
        film.setId(simpleSave(film));
        createLink(film);
        setAdditionalMovieFields(film);
    }

    @Override
    public void delete(Film film) {
        String sqlQuery = "DELETE FROM films WHERE id = ?";
        delLink(film);
        jdbcTemplate.update(sqlQuery, film.getId());
    }

    @Override
    public void update(Film film) {
        String sqlQuery = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());
        delLink(film);
        createLink(film);
        setAdditionalMovieFields(film);
    }
}