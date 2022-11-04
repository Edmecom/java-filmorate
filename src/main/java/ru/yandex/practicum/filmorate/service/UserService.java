package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.validation.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserStorage storage;

    @Autowired
    public UserService(InMemoryUserStorage storage) {
        this.storage = storage;
    }

    public void create(User user) {
        storage.create(user);
    }

    public void addFriend(long userId, long friendId) {
        User user = storage.getById(userId);
        User friend = storage.getById(friendId);
        user.getFriendIds().add(friend.getId());
        friend.getFriendIds().add(user.getId());
        storage.update(user);
        storage.update(friend);
    }

    public void removeFriend(long userId, long friendId) {
        User user = storage.getById(userId);
        User friend = storage.getById(friendId);
        storage.getById(friendId);
        user.getFriendIds().remove(friendId);
        friend.getFriendIds().remove(userId);
        storage.update(user);
        storage.update(friend);
    }

    public List<User> getFriends(long id) {
        List<User> friends = new ArrayList<>();
        User user = storage.getById(id);
        for (Long idFriend : user.getFriendIds()) {
            User friend = storage.getById(idFriend);
            friends.add(friend);
        }
        return friends;
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        User user = storage.getById(userId);
        User friend = storage.getById(friendId);
        List<User> commonFriends = new ArrayList<>();
        user.getFriendIds().stream()
                .filter(idUser -> friend.getFriendIds().contains(idUser))
                .forEach(idUser -> commonFriends.add(storage.getById(idUser)));
        return commonFriends;
    }

    public User getById(long id) {
        return storage.getById(id);
    }

    public void update(User user) {
        storage.update(user);
    }

    public void delete(long id) {
        storage.delete(id);
    }

    public List<User> getAll() {
        return storage.getAll();
    }

    public boolean isContainsUser(long id) {
        return storage.isContains(id);
    }
}
