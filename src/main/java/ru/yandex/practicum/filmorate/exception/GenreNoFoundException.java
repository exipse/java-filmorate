package ru.yandex.practicum.filmorate.exception;

public class GenreNoFoundException extends RuntimeException {

    public GenreNoFoundException(String message) {
        super(message);
    }
}
