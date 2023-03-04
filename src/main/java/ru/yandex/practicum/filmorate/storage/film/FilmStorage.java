package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;

public interface FilmStorage {

    /**
     * Сохраннеие фильма
     */
    Film create(Film film);

    /**
     * Обновление фильма
     */
    Film update(Film film);

    /**
     * Получение фильма по id
     */
    Film getFilm(int id);

    /**
     * Получение фильмов
     */
    List<Film> getAllFilms();

    /**
     * Получение мапы фильмов
     */
    HashMap<Integer, Film> getFilmMap();

    /**
     * Добавление лайка
     */
    void addLike(int filmId, int userId);

    /**
     * Удаление лайка
     */
    void deleteLike(int filmId, int userId);

    /**
     * Получение популярных фильмов
     */
    List<Film> viewPopularFilms(int count);

}
