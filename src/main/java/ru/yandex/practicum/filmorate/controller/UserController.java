package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.Validator;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private Integer generatedId = 0;
    private final Validator validator = new Validator();


    //создание пользователя;
    @PostMapping
    public User createUser(@Valid @RequestBody User user) throws ValidationException {
        validator.userValidate(user);
        user.setId(++generatedId);
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
}
