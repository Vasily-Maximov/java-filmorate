package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Mpa extends AbstractModel {

    @NotNull(groups = {CreateGroup.class, UpdateGroup.class})
    private String name;

    public Mpa(Integer id, String name) {
        super.setId(id);
        this.name = name;
    }
}