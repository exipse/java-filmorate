package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNoFoundException;
import ru.yandex.practicum.filmorate.exception.UserNoFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    static final int MAX_LENGTH_DESCRIPTION = 200;
    static final LocalDate FILM_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film create(Film film) {
        validateObject(film);
        filmStorage.create(film);
        log.info("Фильм с id = " + film.getId() + " сохранен");
        return film;
    }

    public Film update(Film film) {
        validateObject(film);
        filmStorage.update(film);
        log.info("Данные по фильму с id = " + film.getId() + " обновлены");
        return film;
    }

    public Film getFilm(int id) {
        return filmStorage.getFilm(id).orElseThrow(() ->
                new FilmNoFoundException("Фильм по указанному id " + id + " не найден"));
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    //Добавление лайка
    public void addLike(int filmId, int userId) {
        ifExistUserAndFilm(filmId, userId);
        filmStorage.addLike(filmId, userId);
        log.info(String.format(" Пользователь %d добавил лайк фильму %d ", userId, filmId));
    }

    //удаление лайка
    public void deleteLike(int filmId, int userId) {
        ifExistUserAndFilm(filmId, userId);
        filmStorage.deleteLike(filmId, userId);
        log.info(String.format(" Пользователь %d удалил лайк фильму %d ", userId, filmId));
    }

    //вывод 10 полулярных фильмов по кол-ву лайков ( 1 пользователь -1 лайк на фильм)
    public List<Film> viewPopularFilms(int count) {
        return filmStorage.viewPopularFilms(count);
    }

    public List<Genre> getGenreList() {
        return filmStorage.getGenreList();
    }

    public Genre getGenreById(Integer id) {
        ifExistGenreById(id);
        log.info("Жанр с id = " + id + " возвращен");
        return filmStorage.getGenreById(id);
    }

    public List<MPA> getMpaList() {
        return filmStorage.getMpaList();
    }

    public MPA getMpaById(Integer id) {
        ifExistMPAById(id);
        log.info("Рейтинг с id = " + id + " возвращен");
        return filmStorage.getMpaById(id);
    }


    private void ifExistUserAndFilm(int filmId, int userId) {
        if (!filmStorage.getFilm(filmId).isPresent()) {
            throw new FilmNoFoundException("Фильма с id = " + filmId + " не существует");
        }
        if (!userStorage.getUser(userId).isPresent()) {
            throw new UserNoFoundException("Юзера с id = " + userId + " не существует");
        }
    }

    private void ifExistGenreById(int id) {
        if (filmStorage.getGenreById(id).equals(null)) {
            throw new ValidationException(String.format("Жанр с id = %s не найден", id));
        }
    }

    private void ifExistMPAById(int id) {
        if (filmStorage.getMpaById(id).equals(null)) {
            throw new ValidationException(String.format("Рейтинг с id = %s не найден", id));
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
