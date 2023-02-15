package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private static int count = 1;
    private final HashMap<Integer, Film> filmMap = new HashMap<>();
    static final int MAX_LENGTH_DESCRIPTION = 200;
    static final LocalDate FILM_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);

    @PostMapping
    public Film create(@RequestBody Film film) {
        validateObject(film);
        film.setId(count);
        count += 1;
        filmMap.put(film.getId(), film);
        log.info("Фильм с id = " + film.getId() + " сохранен");
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        validateObject(film);
        if (!filmMap.containsKey(film.getId())) {
            log.error("Фильма с id = " + film.getId() + " не существует");
            throw new ValidationException("Фильма с id = " + film.getId() + " не существует");
        }
        filmMap.put(film.getId(), film);
        log.info("Данные по фильму с id = " + film.getId() + " обновлены");
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmMap.values());
    }

    private void validateObject(Film film) {
        if (film.getName().isBlank()) {
            log.error("Название не может быть пустым");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > MAX_LENGTH_DESCRIPTION) {
            log.error("Максимальная длина превышает " + MAX_LENGTH_DESCRIPTION + " символов");
            throw new ValidationException("Максимальная длина превышает " + MAX_LENGTH_DESCRIPTION + " символов");
        }
        if (film.getReleaseDate().isBefore(FILM_BIRTHDAY)) {
            log.error("Дата релиза не должна быть раньше " + FILM_BIRTHDAY);
            throw new ValidationException("Дата релиза не должна быть раньше " + FILM_BIRTHDAY);
        }
        if (film.getDuration() < 0) {
            log.error("Продолжительность фильма должна быть положительной");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }

}
