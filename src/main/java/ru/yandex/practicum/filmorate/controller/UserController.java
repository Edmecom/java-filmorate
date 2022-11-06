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
    public ResponseEntity<User> create(@RequestBody final User user) {
        return new ResponseEntity<>(service.create(user), HttpStatus.CREATED);
    }

    //обновление пользователя;
    @PutMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> update(@RequestBody User user) {
        return new ResponseEntity<>(service.update(user), HttpStatus.OK);
    }

    //получение списка всех пользователей.
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") long id) {
        return new ResponseEntity<>(service.getById(id), HttpStatus.OK);
    }

    //добавление в друзья.
    @PutMapping("/users/{id}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        service.addFriend(id, friendId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //удаление из друзей.
    @DeleteMapping("/users/{id}/friends/{friendId}")
    public ResponseEntity<Void>  removeFriend(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        service.removeFriend(id, friendId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //возвращаем список пользователей, являющихся его друзьями.
    @GetMapping("/users/{id}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable("id") long id) {
        return new ResponseEntity<>(service.getFriends(id), HttpStatus.OK);
    }

    //список друзей, общих с другим пользователем.
    @GetMapping("/users/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable("id") long id, @PathVariable("otherId") long otherId) {
        return new ResponseEntity<>(service.getCommonFriends(id, otherId), HttpStatus.OK);
    }
}
