package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.validation.ValidationException;

import ru.yandex.practicum.filmorate.exception.InputDataException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
@Slf4j
@Service
public class UserService {

    private static long id = 0L;

    private final UserStorage storage;

    @Autowired
    public UserService(InMemoryUserStorage storage) {
        this.storage = storage;
    }

    public User create(User user) {
        if(user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if(user.getFriendIds() == null) {
            user.setFriendIds(new HashSet<>());
        }
        validate(user);
        user.setId(getId());
        storage.create(user);
        log.info("Creating user {}", user);
        return user;
    }

    public void addFriend(long userId, long friendId) {
        if(!isContainsUser(id) || !isContainsUser(friendId)) {
            throw new InputDataException("One or both users not found");
        }
        User user = storage.getById(userId);
        User friend = storage.getById(friendId);
        user.getFriendIds().add(friend.getId());
        friend.getFriendIds().add(user.getId());
        storage.update(user);
        storage.update(friend);
        log.info("User {} added user {} as friend", id, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        User user = storage.getById(userId);
        User friend = storage.getById(friendId);
        storage.getById(friendId);
        user.getFriendIds().remove(friendId);
        friend.getFriendIds().remove(userId);
        storage.update(user);
        storage.update(friend);
        log.info("User {} deleted user {} as friend", id, friendId);
    }

    public List<User> getFriends(long id) {
        List<User> friends = new ArrayList<>();
        User user = storage.getById(id);
        for (Long idFriend : user.getFriendIds()) {
            User friend = storage.getById(idFriend);
            friends.add(friend);
        }
        log.info("Get user id={} friends {}", id, friends.size());
        return friends;
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        if (!isContainsUser(userId) || !isContainsUser(friendId)) {
            throw new InputDataException("One of the two friends was not found by his id");
        }
        User user = storage.getById(userId);
        User friend = storage.getById(friendId);
        List<User> commonFriends = new ArrayList<>();
        user.getFriendIds().stream()
                .filter(idUser -> friend.getFriendIds().contains(idUser))
                .forEach(idUser -> commonFriends.add(storage.getById(idUser)));
        log.info("Get user id={} with otherId={} common friends {}", userId, friendId, commonFriends.size());
        return commonFriends;
    }

    public User getById(long id) {
        if(!isContainsUser(id)) {
            throw new InputDataException("User with this id was not found");
        }
        return storage.getById(id);
    }

    public User update(User user) {
        if(!isContainsUser(user.getId())) {
            throw new InputDataException("User with this id was not found");
        }
        if(user.getFriendIds() == null) {
            user.setFriendIds(new HashSet<>());
        }
        if(user.getName().isEmpty()) {
            user.setName(user.getEmail());
        }
        validate(user);
        storage.update(user);
        log.info("Updating user {}", user);
        return user;
    }

    public void delete(long id) {
        storage.delete(id);
    }

    public List<User> getAll() {
        log.info("Get all users");
        return storage.getAll();
    }

    private boolean isContainsUser(long id) {
        return storage.isContains(id);
    }

    private void validate(User user) {
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
    }
}
