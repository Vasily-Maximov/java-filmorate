package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.CreateGroup;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exeption.ObjectValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmApplicationTests {
    private static Validator validator;

    @BeforeEach
    public void setValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void postFilmTest() {
        Film film = new Film(1,"Троя","Историко-приключенческая драма", LocalDate.of(2004,8,
                26), 169);
        Set<ConstraintViolation<Film>> violations = validator.validate(film, CreateGroup.class);
        assertFalse(violations.isEmpty(), "Ошибка при создании фильма, 'id' при создании должен быть пустым");

        film = new Film(null,"","Историко-приключенческая драма", LocalDate.of(2004,8,
                26), 169);
        violations = validator.validate(film, CreateGroup.class);
        assertFalse(violations.isEmpty(), "Ошибка при создании фильма, 'name' при создании не должно быть пустым");

        film = new Film(null,"Троя","Историко-приключенческая драма", LocalDate.of(2004,8,
                26), -1);
        violations = validator.validate(film, CreateGroup.class);
        assertFalse(violations.isEmpty(), "Ошибка при создании фильма, 'duration' должен быть больше 0");

        film = new Film(null,"Троя","Историко-приключенческая драма 000000000011111111112222222222333333333344444" +
                "4444455555555556666666666777777777788888888889999999999000000000011111111112222222222333333333344444444445555555555" +
                "6666666666", LocalDate.of(2004,8,
                26), 169);
        violations = validator.validate(film, CreateGroup.class);
        assertFalse(violations.isEmpty(), "Ошибка при создании фильма, 'description' должен быть не больше 200 символов");

        ObjectValidationException exception = assertThrows(
                ObjectValidationException.class,
                () -> {
                    new Film(null,"Троя","Историко-приключенческая драма", LocalDate.of(1895,12,
                            29), 169);
                    throw new ObjectValidationException("ок");
                });
        assertEquals("ок", exception.getMessage(), "Ошибка при создании фильма, 'releaseDate' раньше 28 " +
                "декабря 1895 года");
    }
}
