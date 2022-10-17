package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int generatedId = 0;

    //добавление фильма;
    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) throws ValidationException {
        film.setId(++generatedId);
        validate(film);
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

    private void validate(Film film) throws ValidationException {

        //название не может быть пустым;
        if (!StringUtils.hasLength(film.getName())) {
            log.error("название не может быть пустым");
            throw new ValidationException("Название фильма не указано");
        }

        //максимальная длина описания — 200 символов;
        if (film.getDescription().length() > 200) {
            log.error("максимальная длина описания — 200 символов");
            throw new ValidationException("Длина описания превышает 200 символов");
        }

        //дата релиза — не раньше 28 декабря 1895 года;
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            log.error("дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза не должна быть раньше 28 декабря 1895 года");
        }
        //продолжительность фильма должна быть положительной.
        if (film.getDuration() <= 0) {
            log.error("продолжительность фильма должна быть положительной");
            throw new ValidationException("Неверно указана продолжительность фильма");
        }
    }
}
