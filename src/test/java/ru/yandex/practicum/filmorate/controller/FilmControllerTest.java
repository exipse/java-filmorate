package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage()));
    }

    @Test
    public void getFilms() throws ValidationException {
        Film film = Film.builder()
                .id(1)
                .name("Фильм 1")
                .description("о гонках")
                .releaseDate(LocalDate.now().minusYears(23))
                .duration(130)
                .listUsersLikes(new HashSet<>())
                .genres(new ArrayList<>())
                .build();
        filmController.create(film);
        assertEquals(1, filmController.getAllFilms().size());
        assertTrue(film.getReleaseDate().isAfter(FilmController.FILM_BIRTHDAY));
    }

    @Test
    public void addEmptyName() throws ValidationException {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        Film film = Film.builder()
                                .id(1)
                                .name("")
                                .description("о гонках")
                                .releaseDate(LocalDate.now().minusYears(23))
                                .duration(130)
                                .listUsersLikes(new HashSet<>())
                                .genres(new ArrayList<>())
                                .build();
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
                        Film film = Film.builder()
                                .id(1)
                                .name("Name")
                                .description("ИнФоИнфоИнФИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИ" +
                                        "нфоИнИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнИнфоИнФоИнфф" +
                                        "оИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнн" +
                                        "фоИнФоИнфоИнИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИн" +
                                        "ИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнИнфоИнФоИнфоИнИ" +
                                        "ИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнИнфоИнФоИнфоИнФоИнфоИн" +
                                        "ФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИн" +
                                        "ФоИнфоИнФоИнфоИнФоИнфоИнИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИн" +
                                        "ФоИнфоИнФоИнфоИнИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфо" +
                                        "ИнИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнИнфоИнФо" +
                                        "ФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИнФоИнфоИн")
                                .releaseDate(LocalDate.now().minusYears(23))
                                .listUsersLikes(new HashSet<>())
                                .genres(new ArrayList<>())
                                .build();
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
                        Film film = Film.builder()
                                .id(1)
                                .name("Фильм 1")
                                .description("о гонках")
                                .releaseDate(LocalDate.of(1895, 12, 27))
                                .duration(130)
                                .listUsersLikes(new HashSet<>())
                                .genres(new ArrayList<>())
                                .build();
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
                        Film film = Film.builder()
                                .id(1)
                                .name("Фильм 1")
                                .description("о гонках")
                                .releaseDate(LocalDate.now().minusYears(23))
                                .duration(-130)
                                .listUsersLikes(new HashSet<>())
                                .genres(new ArrayList<>())
                                .build();
                        filmController.create(film);
                    }
                });
        assertEquals("Продолжительность фильма должна быть положительной",
                exception.getMessage());
    }
}