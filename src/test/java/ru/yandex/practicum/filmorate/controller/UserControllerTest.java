package ru.yandex.practicum.filmorate.controller;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.yandex.practicum.filmorate.model.User;
import javax.xml.bind.ValidationException;
import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private TestRestTemplate template;

    // Неуспешное добавление - эл почта отсутствует.
    @Test
    public void createUserNewUserEmailIsEmpty() {
        User user = new User(
                "",
                "User",
                "",
                LocalDate.of(1987, 10, 22)
        );
        user.setName("User1");
        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity response = template.postForEntity(
                "/users",
                request,
                ValidationException.class
        );
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // Неуспешное добавление - неверный формат эл почты.
    @Test
    public void createUserNewUserEmail() {
        User user = new User(
                "practicum.yandex.ru",
                "User",
                "",
                LocalDate.of(1987, 10, 22)
        );
        user.setName("User1");
        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity response = template.postForEntity(
                "/users",
                request,
                ValidationException.class
        );
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // Неуспешное добавление - неверный логин.
    @Test
    public void createUserNewUserBadLogin() {
        User user = new User(
                "practicum@yandex.ru",
                "User User",
                "",
                LocalDate.of(1987, 10, 22)
        );
        user.setName("User1");
        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity response = template.postForEntity(
                "/users",
                request,
                ValidationException.class
        );
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // Неуспешное добавление - день рождения позже текущей даты.
    @Test
    public void createUserNewUserBirthdayInFuture() {
        User user = new User(
                "practicum@yandex.ru",
                "User",
                "",
                LocalDate.of(2087, 10, 22)
        );
        user.setName("User1");
        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity response = template.postForEntity(
                "/users",
                request,
                ValidationException.class
        );
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}