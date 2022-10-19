package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.Validator;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private final Validator validator = new Validator();
    private int generatedId = 0;

    //добавление фильма;
    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) throws ValidationException {
        validator.filmValidate(film);
        film.setId(++generatedId);
        films.put(film.getId(), film);
        log.debug("Фильм добавлен");
        return film;
    }

    //обновление фильма;
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм не найлен");
        }
        films.put(film.getId(), film);
        log.debug("Данные фильма обновлены");
        return film;
    }

    //получение всех фильмов.
    @GetMapping
    public List<Film> findAll() {
        log.debug("Текущее количество пользователей: {}", films.size());
        return new ArrayList<>(films.values());
    }
}
