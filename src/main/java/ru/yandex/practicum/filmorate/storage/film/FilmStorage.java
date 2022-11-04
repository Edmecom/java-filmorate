package ru.yandex.practicum.filmorate.storage.film;


import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    void create(Film film);

    void update(Film film);

    Film getById(long id);

    void delete(long id);

    List<Film> getAll();

    boolean isContains(long id);
}
