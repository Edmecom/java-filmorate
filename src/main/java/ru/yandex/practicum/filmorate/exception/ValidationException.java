package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends Exception{
    public ValidationException(String s) {
        super(s);
    }
}
