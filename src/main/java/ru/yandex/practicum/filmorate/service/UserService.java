package ru.yandex.practicum.filmorate.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService extends AbstractService<User>{


    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    @SneakyThrows
    @Override
    protected void validate(User data) {
        if (data.getLogin() == null || data.getLogin().isEmpty() || data.getLogin().contains(" ")) {
            throw new ValidationException("User login invalid");
        }
        if (data.getEmail() == null || data.getEmail().isEmpty() || !data.getEmail().contains("@")) {
            throw new ValidationException("User email invalid");
        }
        if (data.getBirthday() != null && data.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("User birthday invalid");
        }
    }

    public void addFriend(long userId, long friendId) {
        final User user = storage.get(userId);
        final User friend = storage.get(friendId);
        user.getFriendIds().add(friendId);
        friend.getFriendIds().add(userId);
    }

    public void removeFriend(long userId, long friendId) {
        final User user = storage.get(userId);
        final User friend = storage.get(friendId);
        storage.get(friendId);
        user.getFriendIds().remove(friendId);
        friend.getFriendIds().remove(userId);
    }

    public List<User> getFriends(long id) {
        List<User> friends = new ArrayList<>();
        final User user = storage.get(id);
        for (Long idFriend : user.getFriendIds()) {
            User friend = storage.get(idFriend);
            friends.add(friend);
        }
        return friends;
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        final User user = storage.get(userId);
        final User friend = storage.get(friendId);
        List<User> commonFriends = new ArrayList<>();
        user.getFriendIds().stream()
                .filter(idUser -> friend.getFriendIds().contains(idUser))
                .forEach(idUser -> commonFriends.add(storage.get(idUser)));
        return commonFriends;
    }
}
