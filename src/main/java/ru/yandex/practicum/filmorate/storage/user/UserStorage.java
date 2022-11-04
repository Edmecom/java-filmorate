package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    void create(User user);

    void update(User user);

    User getById(long id);

    void delete(long id);

    List<User> getAll();

    boolean isContains(long id);
}
