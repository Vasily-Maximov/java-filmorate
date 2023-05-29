package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Slf4j
@Data
@EqualsAndHashCode
public class User extends AbstractModel {

    @Email(groups = {CreateGroup.class, UpdateGroup.class})
    @NotNull(groups = {CreateGroup.class, UpdateGroup.class})
    private String email;
    @NotBlank(groups = {CreateGroup.class, UpdateGroup.class})
    private String login;
    private String name;
    @PastOrPresent(groups = {CreateGroup.class, UpdateGroup.class})
    @NotNull(groups = {CreateGroup.class, UpdateGroup.class})
    private LocalDate birthday;

    public User(Integer id, String email, String login, String name, LocalDate birthday) {
        super.setId(id);
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}