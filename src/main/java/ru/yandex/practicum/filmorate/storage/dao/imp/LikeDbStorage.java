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

    public void addLike(int filmId, int userIid) {
        String sqlQuery = "INSERT INTO likes (film_id, user_id) VALUES(?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userIid);
    }

    public void delLike(int filmId, int userIid) {
        String sqlQuery = "DELETE FROM likes WHERE film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userIid);
    }

    public List<Film> findPopularFilms(Integer count) {
        String sqlQuery = "SELECT COUNT(DISTINCT l.user_id) AS likes, f.id, f.name, f.description, f.release_date, f.duration, " +
                "f.mpa_id, m.name AS mpa_name, fg.genre_id, g.name as genre_name " +
                "FROM films AS f " +
                "LEFT JOIN likes AS l ON f.id = l.film_id " +
                "JOIN mpa AS m ON f.mpa_id = m.id " +
                "LEFT JOIN film_genres AS fg ON f.id = fg.film_id " +
                "LEFT JOIN genres as g ON fg.genre_id = g.id " +
                "GROUP BY f.id, fg.genre_id " +
                "ORDER BY likes DESC, id " +
                "LIMIT ?";
        return filmDbStorage.setFilmsGenres(jdbcTemplate.query(sqlQuery, filmDbStorage.filmsRowMapper(), count));
    }
}