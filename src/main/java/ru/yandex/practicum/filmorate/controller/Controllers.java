package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MessageStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ObjectValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class Controllers<T> {

    private final Map<Integer,T> hashMap = new HashMap<>();

    public List<T> getAll() {
        return new ArrayList<>(hashMap.values());
    }


    public T create(T variable, int id) {
        hashMap.put(id, variable);
        return variable;
    }

    public T update(T variable) {
        if (variable.getClass() == User.class) {
            if (hashMap.containsKey(((User)variable).getId())) {
                log.info(String.format(MessageStatus.PUT_USER.getNameStatus(), ((User)variable).getName()));
                hashMap.put(((User)variable).getId(), variable);
            } else {
                String messageError = String.format(MessageStatus.PUT_USER_ERROR.getNameStatus(), ((User)variable).getId());
                log.error(messageError);
                throw new ObjectValidationException(messageError);
            }
        } else if (variable.getClass() == Film.class) {
            if (hashMap.containsKey(((Film)variable).getId())) {
                log.info(String.format(MessageStatus.PUT_FILM.getNameStatus(), ((Film)variable).getName()));
                hashMap.put(((Film)variable).getId(), variable);
            } else {
                String messageError = String.format(MessageStatus.PUT_FILM_ERROR.getNameStatus(), ((Film)variable).getId());
                log.error(messageError);
                throw new ObjectValidationException(messageError);
            }
        }
        return variable;
    }

}