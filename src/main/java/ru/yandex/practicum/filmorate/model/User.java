package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Null;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class User {
    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    private Integer id;
    @Email(groups = {CreateGroup.class, UpdateGroup.class})
    private String email;
    @NotBlank(groups = {CreateGroup.class, UpdateGroup.class})
    private String login;
    private String name;
    @PastOrPresent(groups = {CreateGroup.class, UpdateGroup.class})
    @NotNull(groups = {CreateGroup.class, UpdateGroup.class})
    private LocalDate birthday;

    public User(Integer id, String email, String login, String name, LocalDate birthday) {
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

}