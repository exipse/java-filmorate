package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@RestController
@Slf4j
@RequestMapping
public class FilmController {

    private final FilmService filmService;
    static final int MAX_LENGTH_DESCRIPTION = 200;
    static final LocalDate FILM_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping("/films")
    public Film update(@RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable int id) {
        return filmService.getFilm(id);
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable("id") int filmId, @PathVariable int userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") int filmId, @PathVariable int userId) {
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> viewPopularFilms(
            @RequestParam(defaultValue = "10") int count) {
        return filmService.viewPopularFilms(count);
    }

    @GetMapping("/genres")
    public List<Genre> getGenreList() {
        return filmService.getGenreList();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable int id) {
        return filmService.getGenreById(id);
    }

    @GetMapping("/mpa")
    public List<MPA> getMpaList() {
        return filmService.getMpaList();
    }

    @GetMapping("mpa/{id}")
    public MPA getMpaById(@PathVariable int id) {
        return filmService.getMpaById(id);
    }

}
