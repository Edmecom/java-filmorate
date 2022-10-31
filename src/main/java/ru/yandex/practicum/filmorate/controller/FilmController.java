package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
public class FilmController {

    FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    //добавление фильма;
    @PostMapping("/films")
    public Film create(@Valid @RequestBody final Film film) {
        log.info("Creating film {}", film);
        return service.create(film);
    }

    //обновление фильма;
    @PutMapping("/films")
    public Film update(@Validated @RequestBody final Film film) {
        log.info("Updating film {}", film);
        return service.update(film);
    }

    //получение всех фильмов.
    @GetMapping("/films")
    public List<Film> getAll() {
        final List<Film> films = service.getAll();
        log.info("Get all films {}", films.size());
        return films;
    }

    @GetMapping("/films/{id}")
    public Film get(@PathVariable long id) {
        log.info("Get film id={}", id);
        return service.get(id);
    }

    //пользователь ставит лайк фильму
    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Add like film id={} from user={}", id, userId);
        service.addLike(id, userId);
    }

    //пользователь удаляет лайк
    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Remove like film id={} from user={}", id, userId);
        service.removeLike(id, userId);
    }

    //возвращает список из первых count фильмов по количеству лайков.
    //если значение параметра count не задано, верните первые 10.
    @GetMapping("/films/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        log.info("Get popular film count=" + count);
        return service.getPopular(count);
    }
}
