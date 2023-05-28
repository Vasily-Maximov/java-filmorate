package ru.yandex.practicum.filmorate.storage.dao.imp;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.ConvertFilm;
import ru.yandex.practicum.filmorate.model.MessageStatus;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dao.AbstractDbStorage;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashSet;

@Component
public class FilmDbStorage extends AbstractDbStorage<Film> implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDbStorage mpaDbStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaDbStorage mpaDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDbStorage = mpaDbStorage;
    }

    private RowMapper<Film> filmRowMapper() {
        return (rs, rowNum) -> {
            Film film = new Film(rs.getInt("id"), rs.getString("name"),
                    rs.getString("description"), rs.getDate("release_date").toLocalDate(),
                    rs.getLong("duration"));
            film.setMpa(new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")));
            do {
                if (rs.getInt("genre_id") != 0) {
                    film.getGenres().add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
                }
            } while (rs.next());
            return film;
        };
    }

    protected RowMapper<Film> filmsRowMapper() {
        return (rs, rowNum) -> {
            Film film = new Film(rs.getInt("id"), rs.getString("name"),
                    rs.getString("description"), rs.getDate("release_date").toLocalDate(),
                    rs.getLong("duration"));
            film.setMpa(new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")));
            if (rs.getInt("genre_id") != 0) {
                film.getGenres().add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
            }
            return film;
        };
    }

    private int simpleSave(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        return simpleJdbcInsert.executeAndReturnKey(ConvertFilm.toMap(film)).intValue();
    }

    protected List<Film> setFilmsGenres(List<Film> films) {
        Map<Integer, Film> mapFilms = new LinkedHashMap<>();
        for (Film film: films) {
            if (mapFilms.containsKey(film.getId())) {
                mapFilms.get(film.getId()).getGenres().addAll(film.getGenres());
            } else {
                mapFilms.put(film.getId(), film);
            }
        }
        return new ArrayList<>(mapFilms.values());
    }

    private void setFilmGenres(Film film) {
        film.setGenres(new HashSet<>((findGenresFilm(film.getId()))));
    }

    private void setFilmMpa(Film film) {
        film.setMpa(mpaDbStorage.findById(film.getMpa().getId()));
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
        String sqlQuery = "INSERT INTO film_genres(film_id, genre_id) VALUES (?, ?)";
        List<Genre> genres = new ArrayList<>(film.getGenres());
        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, film.getId());
                ps.setInt(2, genres.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });
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
        String sqlQuery = "SELECT f.id, f.name, f.description, f.release_date, f.duration, m.id as mpa_id, m.name as mpa_name, " +
                "fg.genre_id, g.name as genre_name " +
                "FROM films as f " +
                "JOIN mpa AS m ON f.mpa_id = m.id " +
                "LEFT JOIN film_genres AS fg ON f.id = fg.film_id " +
                "LEFT JOIN genres as g ON fg.genre_id = g.id " +
                "WHERE f.id = ? " +
                "GROUP BY f.id, fg.genre_id " +
                "ORDER BY f.id";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, filmRowMapper(), id);
        } catch (DataAccessException e) {
            throw new ObjectNotFoundException(String.format(MessageStatus.PUT_FILM_ERROR.getNameStatus(), id));
        }
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT f.id, f.name, f.description, f.release_date, f.duration, m.id as mpa_id, m.name as mpa_name, " +
                "fg.genre_id, g.name as genre_name " +
                "FROM films as f " +
                "JOIN mpa AS m ON f.mpa_id = m.id " +
                "LEFT JOIN film_genres AS fg ON f.id = fg.film_id " +
                "LEFT JOIN genres as g ON fg.genre_id = g.id " +
                "GROUP BY f.id, fg.genre_id " +
                "ORDER BY f.id";
        return setFilmsGenres(jdbcTemplate.query(sqlQuery, filmsRowMapper()));
    }

    @Override
    public void create(Film film) {
        film.setId(simpleSave(film));
        createLink(film);
        setFilmMpa(film);
        setFilmGenres(film);
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
        setFilmMpa(film);
        setFilmGenres(film);
    }
}