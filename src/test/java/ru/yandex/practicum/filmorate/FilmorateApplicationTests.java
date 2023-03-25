package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.CreateGroup;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.UpdateGroup;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FilmorateApplicationTests {

	private static Validator validator;

	@BeforeEach
	public void setValidator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	void postUserTest() {
		User user = new User(null,"kt-62a@mail.ru","kt-62a", "Vasiliy", LocalDate.of(2024,8,
				05));
		Set<ConstraintViolation<User>> violations = validator.validate(user, CreateGroup.class);
		assertFalse(violations.isEmpty(), "Ошибка при создании пользователя, дата рождения указанна в будущем");

		user = new User(1,"kt-62a@mail.ru","kt-62a", "Vasiliy", LocalDate.of(1984,8,
				05));
		violations = validator.validate(user, CreateGroup.class);
		assertFalse(violations.isEmpty(), "Ошибка при создании пользователя, 'id' при создании должен быть пустым");

		user = new User(null,"kt-62a@mail.ru","kt-62a", "Vasiliy", LocalDate.of(1984,8,
				05));
		violations = validator.validate(user, UpdateGroup.class);
		assertFalse(violations.isEmpty(), "Ошибка при изменении пользователя, 'id' при изменении не должен быть" +
				"пустым");

		user = new User(1,"kt-62amail.ru","kt-62a", "Vasiliy", LocalDate.of(1984,8,
				05));
		violations = validator.validate(user, CreateGroup.class);
		assertFalse(violations.isEmpty(), "Ошибка при создании пользователя, 'email' указан неправильно");

		user = new User(null,"kt-62a@mail.ru","  ", "Vasiliy", LocalDate.of(1984,8,
				05));
		violations = validator.validate(user, CreateGroup.class);
		assertFalse(violations.isEmpty(), "Ошибка при создании пользователя, 'login' указан неправильно");
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

		film = new Film(null,"Троя","Историко-приключенческая драма", LocalDate.of(2004,8,
				26), 169);
		violations = validator.validate(film, CreateGroup.class);
		assertTrue(violations.isEmpty(), "Ошибка при создании фильма, 'description' должен быть не больше 200 символов");
	}

}