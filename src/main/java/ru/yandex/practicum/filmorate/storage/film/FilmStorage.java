package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
import java.util.Optional;

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
    Optional<Film> getFilm(int id);

    /**
     * Получение фильмов
     */
    List<Film> getAllFilms();

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

    /**
     * Получение всех жанров
     */
    List<Genre> getGenreList();

    /**
     * Получение жанра по идентификатору
     */
    Genre getGenreById(Integer id);

    /**
     * Получение всех рейтингов
     */
    List<MPA> getMpaList();

    /**
     * Получение рейтингов по идентификатору
     */
    MPA getMpaById(Integer id);

}
