package ru.yandex.practicum.filmorate.exception;

public class UserNoFoundException extends RuntimeException {

    public UserNoFoundException(String message) {
        super(message);
    }
}
