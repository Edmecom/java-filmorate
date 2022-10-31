package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
public class UserController {

    UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    //создание пользователя;
    @PostMapping("/users")
    public User create(@Valid @RequestBody final User user) {
        log.info("Creating user {}", user);
        return service.create(user);
    }

    //обновление пользователя;
    @PutMapping("/users")
    public User update(@Validated @RequestBody User user) {
        log.info("Updating user {}", user);
        return service.update(user);
    }

    //получение списка всех пользователей.
    @GetMapping("/users")
    public List<User> getAll() {
        final List<User> users = service.getAll();
        log.info("Get all users {}", users.size());
        return users;
    }

    //добавление в друзья.
    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("User {} added user {} as friend", id, friendId);
        service.addFriend(id, friendId);
    }

    //удаление из друзей.
    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("User {} deleted user {} as friend", id, friendId);
        service.removeFriend(id, friendId);
    }

    //возвращаем список пользователей, являющихся его друзьями.
    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        final List<User> friends = service.getFriends(id);
        log.info("Get user id={} friends {}", id, friends.size());
        return friends;
    }

    //список друзей, общих с другим пользователем.
    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        final List<User> friends = service.getCommonFriends(id, otherId);
        log.info("Get user id={} with otherId={} common friends {}", id, otherId, friends.size());
        return friends;
    }
}
