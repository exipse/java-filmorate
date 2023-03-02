package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNoFoundException;
import ru.yandex.practicum.filmorate.exception.UserNoFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    static final int MAX_LENGTH_DESCRIPTION = 200;
    static final LocalDate FILM_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Film create(Film film) {
        validateObject(film);
        inMemoryFilmStorage.create(film);
        log.info("Фильм с id = " + film.getId() + " сохранен");
        return film;
    }

    public Film update(Film film) {
        validateObject(film);
        inMemoryFilmStorage.update(film);
        log.info("Данные по фильму с id = " + film.getId() + " обновлены");
        return film;
    }

    public Film getFilm(int id) {
        return inMemoryFilmStorage.getFilm(id);
    }

    public List<Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }

    //Добавление лайка
    public void addLike(int filmId, int userId) {
        ifExistUserAndFilm(filmId, userId);
        inMemoryFilmStorage.addLike(filmId, userId);
        log.info(String.format(" Пользователь %d добавил лайк фильму %d ", userId, filmId));
    }

    //удаление лайка
    public void deleteLike(int filmId, int userId) {
        ifExistUserAndFilm(filmId, userId);
        inMemoryFilmStorage.deleteLike(filmId, userId);
        log.info(String.format(" Пользователь %d удалил лайк фильму %d ", userId, filmId));
    }

    //вывод 10 полулярных фильмов по кол-ву лайков ( 1 пользователь -1 лайк на фильм)
    public List<Film> viewPopularFilms(int count) {
        return inMemoryFilmStorage.viewPopularFilms(count);
    }

    private void ifExistUserAndFilm(int filmId, int userId) {
        if (!inMemoryFilmStorage.getFilmMap().containsKey(filmId)) {
            throw new FilmNoFoundException("Фильма с id = " + userId + " не существует");
        }
        if (!inMemoryUserStorage.getMapUsers().containsKey(userId)) {
            throw new UserNoFoundException("Юзера с id = " + userId + " не существует");
        }
    }

    private void validateObject(Film film) {
        if (film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > MAX_LENGTH_DESCRIPTION) {
            throw new ValidationException("Максимальная длина превышает " + MAX_LENGTH_DESCRIPTION + " символов");
        }
        if (film.getReleaseDate().isBefore(FILM_BIRTHDAY)) {
            throw new ValidationException("Дата релиза не должна быть раньше " + FILM_BIRTHDAY);
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}
