package ru.yandex.practicum.filmorate.model;

import java.util.HashMap;
import java.util.Map;

public class ConvertFilm {

    public static Map<String, Object> toMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("duration", film.getDuration());
        values.put("release_date", film.getReleaseDate());
        values.put("mpa_id", film.getMpa().getId());
        return values;
    }
}