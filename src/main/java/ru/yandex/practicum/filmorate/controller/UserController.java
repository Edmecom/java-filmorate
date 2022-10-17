package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int generatedId = 0;

    //создание пользователя;
    @PostMapping
    public User createUser(@Valid @RequestBody User user) throws ValidationException {
        user.setId(++generatedId);
        validate(user);
        users.put(user.getId(), user);
        log.debug("Пользователь сохранен");
        return user;
    }

    //обновление пользователя;
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException{
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь не найдет");
        }
        users.put(user.getId(), user);
        log.debug("Данные пользователя обновлены");
        return user;
    }

    //получение списка всех пользователей.
    @GetMapping
    public List<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    //метод для обработки ошибок валидации
    private void validate(User user) throws ValidationException {

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
}
