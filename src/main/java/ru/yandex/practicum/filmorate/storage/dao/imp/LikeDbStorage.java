package ru.yandex.practicum.filmorate.storage.dao.imp;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

@Component
public class LikeDbStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmDbStorage filmDbStorage;

    public LikeDbStorage(JdbcTemplate jdbcTemplate, FilmDbStorage filmDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmDbStorage = filmDbStorage;
    }

    public void addLike(int film_id, int user_id) {
        String sqlQuery = "INSERT INTO likes (film_id, user_id) VALUES(?, ?)";
        jdbcTemplate.update(sqlQuery, film_id, user_id);
    }

    public void delLike(int film_id, int user_id) {
        String sqlQuery = "DELETE FROM likes WHERE film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, film_id, user_id);
    }

    public List<Film> findPopularFilms(Integer count) {
        String sqlQuery = "SELECT COUNT(DISTINCT l.user_id) AS likes, f.id, f.name, f.description, f.release_date, f.duration," +
                "f.mpa_id, m.name AS mpa_name " +
                "FROM films AS f " +
                "LEFT JOIN likes AS l ON f.id = l.film_id " +
                "JOIN mpa AS m ON f.mpa_id = m.id " +
                "GROUP BY f.id " +
                "ORDER BY likes DESC, id " +
                "LIMIT ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, filmDbStorage.filmRowMapper(), count);
        for (Film film: films) {
            filmDbStorage.setAdditionalMovieFields(film);
        }
        return films;
    }
}