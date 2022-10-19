package ru.yandex.practicum.filmorate.validators;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
@Slf4j
public class Validator {

    public void userValidate(User user) throws ValidationException {

        //электронная почта не может быть пустой и должна содержать символ @;
        if (!StringUtils.hasLength(user.getEmail()) || !user.getEmail().contains("@")) {
            log.error("электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("электронная почта не указана, либо не содержит символ @");
        }

        //логин не может быть пустым и содержать пробелы;
        if (!StringUtils.hasLength(user.getLogin()) || user.getLogin().contains(" ")) {
            log.error("логин не может быть пустым и содержать пробелы;");
            throw new ValidationException("логин не указан, либо содержит пробел");
        }

        //имя для отображения может быть пустым — в таком случае будет использован логин;
        if (!StringUtils.hasLength(user.getName())) {
            log.error("имя для отображения может быть пустым — в таком случае будет использован логин");
            user.setName(user.getLogin());
        }

        //дата рождения не может быть в будущем.
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("дата рождения не может быть в будущем");
            throw new ValidationException("Неверно указана дата рождения");
        }
    }

    public void filmValidate(Film film) throws ValidationException {

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
