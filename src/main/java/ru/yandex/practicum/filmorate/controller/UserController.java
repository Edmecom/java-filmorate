package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    //создание пользователя;
    @PostMapping("/users")
    @ResponseBody
    public ResponseEntity<User> create(@RequestBody final User user) {
        return new ResponseEntity<>(service.create(user), HttpStatus.CREATED);
    }

    //обновление пользователя;
    @PutMapping("/users")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> update(@RequestBody User user) {
        return new ResponseEntity<>(service.update(user), HttpStatus.OK);
    }

    //получение списка всех пользователей.
    @GetMapping("/users")
    public List<User> getAll() {
        return service.getAll();
    }

    @GetMapping("/users/{id}")
    public User getById(@PathVariable("id") long id) {
        return service.getById(id);
    }

    //добавление в друзья.
    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        service.addFriend(id, friendId);
    }

    //удаление из друзей.
    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        service.removeFriend(id, friendId);
    }

    //возвращаем список пользователей, являющихся его друзьями.
    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable("id") long id) {
        return service.getFriends(id);
    }

    //список друзей, общих с другим пользователем.
    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") long id, @PathVariable("otherId") long otherId) {
        return service.getCommonFriends(id, otherId);
    }
}
