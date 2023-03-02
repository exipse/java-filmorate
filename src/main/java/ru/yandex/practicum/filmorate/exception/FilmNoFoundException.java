package ru.yandex.practicum.filmorate.exception;

public class FilmNoFoundException extends RuntimeException {

    public FilmNoFoundException(String message) {
        super(message);
    }
}
