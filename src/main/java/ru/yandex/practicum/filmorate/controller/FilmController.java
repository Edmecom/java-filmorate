package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InputDataException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FilmController {

    private static final LocalDate dateFirstFilm = LocalDate.of(1895, 12, 28);

    private static final int maxLengthDescription = 200;

    private static long id = 0L;

    private final FilmService service;

    //добавление фильма;
    @PostMapping("/films")
    @ResponseBody
    public ResponseEntity<Film> create(@Validated @RequestBody Film film) {
        if (film.getUserIds() == null) {
            film.setUserIds(new HashSet<>());
        }
        validate(film);
        film.setId(getId());
        service.create(film);
        log.info("Creating film {}", film);
        return new ResponseEntity<>(film, HttpStatus.CREATED);
    }

    //обновление фильма;
    @PutMapping("/films")
    @ResponseBody
    public ResponseEntity<Film> update(@Validated @RequestBody final Film film) {
        if (film.getUserIds() == null) {
            film.setUserIds(new HashSet<>());
        }
        if (!service.isContainsFilm(film.getId())) {
            throw new InputDataException("Film with this id was not found");
        }
        validate(film);
        service.update(film);
        log.info("Updating film {}", film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    //получение всех фильмов.
    @GetMapping("/films")
    @ResponseBody
    public List<Film> getAll() {
        final List<Film> films = service.getAll();
        log.info("Get all films {}", films.size());
        return films;
    }

    @GetMapping("/films/{id}")
    @ResponseBody
    public Film getById(@PathVariable("id") long id) {
        if (!service.isContainsFilm(id)) {
            throw new InputDataException("Film with this id was not found");
        }
        log.info("Get film id={}", id);
        return service.getById(id);
    }

    //пользователь ставит лайк фильму
    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        log.info("Add like film id={} from user={}", id, userId);
        service.addLike(id, userId);
    }

    //пользователь удаляет лайк
    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        if (!service.isContainsFilm(id)) {
            throw new InputDataException("Film with this id was not found");
        }
        if (userId < 0) {
            throw new InputDataException("User with this id was not found");
        }
        log.info("Remove like film id={} from user={}", id, userId);
        service.removeLike(id, userId);
    }

    //возвращает список из первых count фильмов по количеству лайков.
    //если значение параметра count не задано, верните первые 10.
    @GetMapping("/films/popular")
    @ResponseBody
    public List<Film> getPopular(@RequestParam(required = false) String count) {
        log.info("Get popular film count=" + count);
        String POPULAR_FILMS = "10";
        return service.getPopular(Objects.requireNonNullElse(count, POPULAR_FILMS));
    }

    private void validate(Film film) {
        if (film.getName().isBlank()) {
            log.warn("Error in Film input. Name is empty");
            throw new ValidationException("Film name invalid");
        }
        if (film.getDescription().length() > maxLengthDescription) {
            log.warn("Error in Film input. Exceeded maximum allowed film description in "
                    + maxLengthDescription + " symbols. Current film description length "
                    + film.getDescription().length() + " symbols.");
            throw new ValidationException("Film description invalid");
        }
        if (film.getReleaseDate().isBefore(dateFirstFilm)) {
            log.warn("Error in Film input. Incorrect release date " + film.getReleaseDate()
                    + ". Release should be later " + dateFirstFilm);
            throw new ValidationException("Film release date invalid");
        }
        if (film.getDuration() <= 0) {
            log.warn("Error in Film input. Film duration must be positive.");
            throw new ValidationException("Film duration invalid");
        }
    }

    public long getId() {
        return ++id;
    }
}
