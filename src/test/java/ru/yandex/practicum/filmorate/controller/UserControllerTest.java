package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private TestRestTemplate template;

    private User createTestUser() {
        return new User(
                "practicum@yandex.ru",
                "User",
                "User1",
                LocalDate.of(1987, 10, 22)
        );
    }

    // Неуспешное добавление - эл почта отсутствует.
    @Test
    public void createUserNewUserEmailIsEmpty() throws ValidationException {
        User user = createTestUser();
        user.setEmail(null);
        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity<ValidationException> response = template.postForEntity(
                "/users",
                request,
                ValidationException.class
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // Неуспешное добавление - неверный формат эл почты.
    @Test
    public void createUserNewUserEmail() {
        User user = createTestUser();
        user.setEmail("practicum.yandex.ru");
        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity<User> response = template.postForEntity(
                "/users",
                request,
                User.class
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // Неуспешное добавление - неверный логин.
    @Test
    public void createUserNewUserBadLogin() {
        User user = createTestUser();
        user.setLogin(null);
        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity<User> response = template.postForEntity(
                "/users",
                request,
                User.class
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // Неуспешное добавление - день рождения позже текущей даты.
    @Test
    public void createUserNewUserBirthdayInFuture() {
        User user = createTestUser();
        user.setId(1);
        user.setBirthday(LocalDate.of(2087, 10, 22));
        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity<ValidationException> response = template.postForEntity(
                "/users",
                request,
                ValidationException.class
        );
        //assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}