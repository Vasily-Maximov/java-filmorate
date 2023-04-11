package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exeption.ObjectValidationException;

import javax.validation.constraints.Null;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Data
public class Film {
    private static final LocalDate BEGIN_DATE = LocalDate.of(1895,12,28);
    @Null(groups = CreateGroup.class)
    @NotNull(groups = UpdateGroup.class)
    private Integer id;
    @NotBlank(groups = {CreateGroup.class, UpdateGroup.class})
    private String name;
    @NotBlank(groups = {CreateGroup.class, UpdateGroup.class})
    @Size(max = 200, groups = {CreateGroup.class, UpdateGroup.class})
    private String description;
    @NotNull(groups = {CreateGroup.class, UpdateGroup.class})
    private LocalDate releaseDate;
    @Positive(groups = {CreateGroup.class, UpdateGroup.class})
    private long duration;
    private Set<Long> likes = new HashSet<>();

    public Film(Integer id, String name, String description, LocalDate releaseDate, long duration) {
        checkReleaseDate(name, releaseDate);
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    private void checkReleaseDate(String name, LocalDate releaseDate) {
        if (!BEGIN_DATE.isBefore(releaseDate)) {
            String messageError = String.format(MessageStatus.ERROR_DATE.getNameStatus(), name);
            log.error(messageError);
            throw new ObjectValidationException(messageError);
        }
    }
}