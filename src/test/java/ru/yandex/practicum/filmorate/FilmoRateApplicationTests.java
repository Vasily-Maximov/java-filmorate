package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.imp.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.imp.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.imp.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.imp.UserDbStorage;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmoRateApplicationTests {

    private final FilmDbStorage filmDbStorage;
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final UserDbStorage userDbStorage;

    @Test
    public void testFilm() {
        Film film = new Film(1,"Троя","Историко-приключенческая драма", LocalDate.of(2004,8,
                26), 169);
        film.setMpa(mpaDbStorage.findById(1));
        film.getGenres().addAll(List.of(genreDbStorage.findById(2),genreDbStorage.findById(5)));
        filmDbStorage.create(film);
        assertEquals(filmDbStorage.findById(1), film, "Фильм не создан");

        film = new Film(2,"Хоббит","Нежданное путешествие", LocalDate.of(2012,12,
                19), 150);
        film.setMpa(mpaDbStorage.findById(2));
        film.getGenres().addAll(List.of(genreDbStorage.findById(3),genreDbStorage.findById(6)));
        filmDbStorage.create(film);
        assertEquals(filmDbStorage.getAll().size(), 2, "Получили неверное количество фильмов");

        film.setName("Хоббит - Нежданное путешествие");
        filmDbStorage.update(film);
        assertEquals(filmDbStorage.findById(2), film, "Фильм не изменился");

        filmDbStorage.delete(film);
        assertEquals(filmDbStorage.getAll().size(), 1, "Фильм не удален");

    }

    @Test
    public void testUser() {
        User user = new User(1,"kt-62a@mail.ru","kt-62a@mail.ru", "Vasiliy", LocalDate.of(1984,8,
                5));
        userDbStorage.create(user);
        assertEquals(userDbStorage.findById(1), user, "Пользователь не создан");

        user = new User(2,"62a@mail.ru","62a@mail.ru", "Vasiliy", LocalDate.of(1994,8,
                5));
        userDbStorage.create(user);
        assertEquals(userDbStorage.getAll().size(), 2, "Получили неверное количество пользователей");

        user.setName("Vasiliy Maksimov");
        userDbStorage.update(user);
        assertEquals(userDbStorage.findById(2), user, "Пользователь не изменился");

        userDbStorage.delete(user);
        assertEquals(userDbStorage.getAll().size(), 1, "Пользователь не удален");

    }
}