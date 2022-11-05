package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InputDataException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    //private static long id = 0L;

    private final UserService service;

    //создание пользователя;
    @PostMapping("/users")
    @ResponseBody
    public ResponseEntity<User> create(@RequestBody final User user) {
        /*log.info("Creating user {}", user);
        if(user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if(user.getFriendIds() == null) {
            user.setFriendIds(new HashSet<>());
        }
        validate(user);
        user.setId(getId());
        service.create(user);*/
        return new ResponseEntity<>(service.create(user), HttpStatus.CREATED);
    }

    //обновление пользователя;
    @PutMapping("/users")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User> update(@RequestBody User user) {
        /*if(!service.isContainsUser(user.getId())) {
            throw new InputDataException("User with this id was not found");
        }
        if(user.getFriendIds() == null) {
            user.setFriendIds(new HashSet<>());
        }
        if(user.getName().isEmpty()) {
            user.setName(user.getEmail());
        }
        validate(user);
        service.update(user);
        log.info("Updating user {}", user);*/
        return new ResponseEntity<>(service.update(user), HttpStatus.OK);
    }

    //получение списка всех пользователей.
    @GetMapping("/users")
    public List<User> getAll() {
        /*List<User> users = service.getAll();
        log.info("Get all users {}", users.size());*/
        return service.getAll();
    }

    @GetMapping("/users/{id}")
    public User getById(@PathVariable("id") long id) {
        /*if(!service.isContainsUser(id)) {
            throw new InputDataException("User with this id was not found");
        }*/
        return service.getById(id);
    }

    //добавление в друзья.
    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        /*log.info("User {} added user {} as friend", id, friendId);
        if(!service.isContainsUser(id) || !service.isContainsUser(friendId)) {
            throw new InputDataException("One or both users not found");
        }*/
        service.addFriend(id, friendId);
    }

    //удаление из друзей.
    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        //log.info("User {} deleted user {} as friend", id, friendId);
        service.removeFriend(id, friendId);
    }

    //возвращаем список пользователей, являющихся его друзьями.
    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable("id") long id) {
        /*List<User> friends = service.getFriends(id);
        log.info("Get user id={} friends {}", id, friends.size());*/
        return service.getFriends(id);
    }

    //список друзей, общих с другим пользователем.
    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") long id, @PathVariable("otherId") long otherId) {
        /*if (!service.isContainsUser(id) || !service.isContainsUser(otherId)) {
            throw new InputDataException("One of the two friends was not found by his id");
        }
        List<User> friends = service.getCommonFriends(id, otherId);
        log.info("Get user id={} with otherId={} common friends {}", id, otherId, friends.size());*/
        return service.getCommonFriends(id, otherId);
    }

    /*private void validate(User user) {
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.warn("Error in User input. Login is empty or contains spaces.");
            throw new ValidationException("User login invalid");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.warn("Error in User input. Email is empty or does not contain @.");
            throw new ValidationException("User email invalid");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Error in User input. Date of birth is in the future.");
            throw new ValidationException("User birthday invalid");
        }
    }

    public long getId() {
        return ++id;
    }*/
}
