package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.validation.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {



    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage fileStorage) {
        this.filmStorage = fileStorage;
    }



    public void create(Film film) {
        filmStorage.create(film);
    }

    public void addLike(long id, long userId) {
        Film film = filmStorage.getById(id);
        film.getUserIds().add(userId);
        filmStorage.update(film);
    }

    public void removeLike(long id, long userId) {
        Film film = filmStorage.getById(id);
        film.getUserIds().remove(userId);
        filmStorage.update(film);
    }

    public List<Film> getPopular(String count) {
        return filmStorage.getAll().stream()
                .filter(film -> film.getUserIds() != null)
                .sorted(Comparator.comparing(film -> film.getUserIds().size(), Comparator.reverseOrder()))
                .limit(Long.parseLong(count))
                .collect(Collectors.toList());
    }

    public Film getById(long id) {
        return filmStorage.getById(id);
    }

    public void update(Film film) {
        filmStorage.update(film);
    }

    public void delete(long id) {
        filmStorage.delete(id);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public boolean isContainsFilm(long id) {
        return filmStorage.isContains(id);
    }
}
