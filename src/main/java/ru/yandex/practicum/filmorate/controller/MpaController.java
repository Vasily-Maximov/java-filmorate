package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.model.MessageStatus;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public List<Mpa> getAllMpa() {
        log.info(String.format(MessageStatus.GET_ALL_MPA.getNameStatus()));
        return mpaService.getAll();
    }

    @GetMapping("/{id}")
    public Mpa findById(@PathVariable(value = "id") Integer idMpa) {
        log.info(String.format(MessageStatus.GET_MPA_BY_ID.getNameStatus(), idMpa));
        return mpaService.findById(idMpa);
    }
}