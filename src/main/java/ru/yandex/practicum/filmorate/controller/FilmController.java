package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FilmController {

    private final FilmService service;

    //добавление фильма;
    @PostMapping("/films")
    @ResponseBody
    public ResponseEntity<Film> create(@Validated @RequestBody Film film) {
        return new ResponseEntity<>(service.create(film), HttpStatus.CREATED);
    }

    //обновление фильма;
    @PutMapping("/films")
    @ResponseBody
    public ResponseEntity<Film> update(@Validated @RequestBody final Film film) {
        return new ResponseEntity<>(service.update(film), HttpStatus.OK);
    }

    //получение всех фильмов.
    @GetMapping("/films")
    @ResponseBody
    public List<Film> getAll() {
        return service.getAll();
    }

    @GetMapping("/films/{id}")
    @ResponseBody
    public Film getById(@PathVariable("id") long id) {
        return service.getById(id);
    }

    //пользователь ставит лайк фильму
    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        service.addLike(id, userId);
    }

    //пользователь удаляет лайк
    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        service.removeLike(id, userId);
    }

    //возвращает список из первых count фильмов по количеству лайков.
    //если значение параметра count не задано, верните первые 10.
    @GetMapping("/films/popular")
    @ResponseBody
    public List<Film> getPopular(@RequestParam(required = false) String count) {
        return service.getPopular(count);
    }
}
