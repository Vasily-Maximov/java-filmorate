package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Genre extends AbstractModel {
    @NotNull(groups = {CreateGroup.class, UpdateGroup.class})
    private String name;

    public Genre(Integer id, String name) {
        super.setId(id);
        this.name = name;
    }
}