package ru.yandex.practicum.filmorate.exception;

public class InputDataException extends IllegalArgumentException{
    public InputDataException(final String s) {
        super(s);
    }
}
