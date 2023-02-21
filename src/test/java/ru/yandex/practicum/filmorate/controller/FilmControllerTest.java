package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController();
    }

    @Test
    public void getFilms() throws ValidationException {
        Film film = new Film("Человек-паук", "1 часть",
                LocalDate.of(2002, 04, 30), 121);
        Film film2 = new Film("Властилин Колец", "Братство Кольца",
                LocalDate.of(2001, 12, 19), 178);
        filmController.create(film);
        filmController.create(film2);
        assertEquals(2, filmController.getAllFilms().size());
        assertTrue(film.getReleaseDate().isAfter(FilmController.FILM_BIRTHDAY));
    }

    @Test
    public void addEmptyName() throws ValidationException {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        Film film = new Film("", "1 часть",
                                LocalDate.of(2001, 12, 19), 178);
                        filmController.create(film);
                    }
                });
        assertEquals("Название не может быть пустым", exception.getMessage());
    }


    @Test
    public void addLongDescription() throws ValidationException {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        Film film = new Film("Человек-паук", "ИнФоИнфоИнФоИн" +
                                "ИнФоИнфоИнФоИнфоИнФоИИнФоИнфоИнФоИнфоИИнФоИнфоИнФоИнфоИнФоИнф" +
                                "оИнФоИнфоИнФоИнфофоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфонФоИнфоИнФоИнф" +
                                "оИнФоИнфофоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфонфоИнФоИнфоИнФоИнфофоИнФо" +
                                "ИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИ",
                                LocalDate.of(2001, 12, 19), 178);
                        filmController.create(film);
                    }
                });
        assertEquals("Максимальная длина превышает " + FilmController.MAX_LENGTH_DESCRIPTION + " символов",
                exception.getMessage());
    }

    @Test
    public void validateNegativeRelease() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        Film film = new Film("Человек-паук", "ИнФо",
                                LocalDate.of(1895, 12, 27), 178);
                        filmController.create(film);
                    }
                });
        assertEquals("Дата релиза не должна быть раньше " + FilmController.FILM_BIRTHDAY,
                exception.getMessage());
    }

    @Test
    public void validateNegativeDuration() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        Film film = new Film("Человек-паук", "ИнФо",
                                LocalDate.of(2001, 12, 19), -178);
                        filmController.create(film);
                    }
                });
        assertEquals("Продолжительность фильма должна быть положительной",
                exception.getMessage());
    }
}