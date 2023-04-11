package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exeption.ObjectValidationException;

import javax.validation.constraints.Null;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Data
public class User {
    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    private Integer id;
    @Email(groups = {CreateGroup.class, UpdateGroup.class})
    @NotNull(groups = {CreateGroup.class, UpdateGroup.class})
    private String email;
    @NotBlank(groups = {CreateGroup.class, UpdateGroup.class})
    private String login;
    private String name;
    @PastOrPresent(groups = {CreateGroup.class, UpdateGroup.class})
    @NotNull(groups = {CreateGroup.class, UpdateGroup.class})
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();

    public User(Integer id, String email, String login, String name, LocalDate birthday) {
        checkLogin(login);
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = checkName(name);
        this.birthday = birthday;
    }

    public String checkName(String name) {
        if (name == null || name.isBlank()) {
            return this.login;
        } else {
            return name;
        }
    }

    public void checkLogin(String login) {
        if (login.trim().contains(" ")) {
            String messageError = String.format(MessageStatus.ERROR_LOGIN.getNameStatus(), login);
            log.error(messageError);
            throw new ObjectValidationException(messageError);
        }
    }

}