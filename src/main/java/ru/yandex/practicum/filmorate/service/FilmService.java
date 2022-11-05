package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.validation.ValidationException;

import ru.yandex.practicum.filmorate.exception.InputDataException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Slf4j
@Service
public class FilmService {

    private static final LocalDate dateFirstFilm = LocalDate.of(1895, 12, 28);

    private static final int maxLengthDescription = 200;

    private static long id = 0L;

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage fileStorage) {
        this.filmStorage = fileStorage;
    }



    public Film create(Film film) {
        if (film.getUserIds() == null) {
            film.setUserIds(new HashSet<>());
        }
        validate(film);
        film.setId(getId());
        filmStorage.create(film);
        log.info("Creating film {}", film);
        return film;
    }

    public void addLike(long id, long userId) {
        Film film = filmStorage.getById(id);
        film.getUserIds().add(userId);
        filmStorage.update(film);
        log.info("Add like film id={} from user={}", id, userId);
    }

    public void removeLike(long id, long userId) {
        if (!isContainsFilm(id)) {
            throw new InputDataException("Film with this id was not found");
        }
        if (userId < 0) {
            throw new InputDataException("User with this id was not found");
        }
        Film film = filmStorage.getById(id);
        film.getUserIds().remove(userId);
        filmStorage.update(film);
        log.info("Remove like film id={} from user={}", id, userId);
    }

    public List<Film> getPopular(String count) {
        log.info("Get popular film count=" + count);
        String POPULAR_FILMS = "10";
        return filmStorage.getAll().stream()
                .filter(film -> film.getUserIds() != null)
                .sorted(Comparator.comparing(film -> film.getUserIds().size(), Comparator.reverseOrder()))
                .limit(Long.parseLong(Objects.requireNonNullElse(count, POPULAR_FILMS)))
                .collect(Collectors.toList());
    }

    public Film getById(long id) {
        if (!isContainsFilm(id)) {
            throw new InputDataException("Film with this id was not found");
        }
        log.info("Get film id={}", id);
        return filmStorage.getById(id);
    }

    public Film update(Film film) {
        if (film.getUserIds() == null) {
            film.setUserIds(new HashSet<>());
        }
        if (!isContainsFilm(film.getId())) {
            throw new InputDataException("Film with this id was not found");
        }
        validate(film);
        filmStorage.update(film);
        log.info("Updating film {}", film);
        return film;
    }

    public void delete(long id) {
        filmStorage.delete(id);
    }

    public List<Film> getAll() {
        log.info("Get all films");
        return filmStorage.getAll();
    }

    private boolean isContainsFilm(long id) {
        return filmStorage.isContains(id);
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
