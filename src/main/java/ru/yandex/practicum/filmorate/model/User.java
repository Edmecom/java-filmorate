package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.Null;
import java.time.LocalDate;
@Data
public class User {
    @Null
    private int id;
    @Email
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    public User(String email, @NonNull String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
