package ru.yandex.practicum.filmorate.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService extends AbstractService<Film>{

    UserService userStorage;

    @Autowired
    public FilmService(FilmStorage storage, UserService userStorage) {
        this.storage = storage;
        this.userStorage = userStorage;
    }

    @SneakyThrows
    @Override
    protected void validate(Film data) {
        if (data.getName() == null || data.getName().isEmpty()) {
            throw new ValidationException("Film name invalid");
        }
        if (data.getDescription() != null && data.getDescription().length() > 200) {
            throw new ValidationException("Film description invalid");
        }
        if (data.getReleaseDate() == null && data.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            throw new ValidationException("Film release date invalid");
        }
        if (data.getDuration() <= 0) {
            throw new ValidationException("Film duration invalid");
        }
    }

    public void addLike(long id, long userId) {
        final Film film = storage.get(id);
        userStorage.get(userId);
        film.addLike(userId);
    }

    public void removeLike(long id, long userId) {
        final Film film = storage.get(id);
        userStorage.get(userId);
        film.removeLike(userId);
    }

    public List<Film> getPopular(int count) {
        return storage.getAll().stream()
                .sorted(Comparator.comparingLong(Film::getRate).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
