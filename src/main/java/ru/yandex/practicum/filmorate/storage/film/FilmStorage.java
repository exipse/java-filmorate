package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;

public interface FilmStorage {

    //сохраннеие фильма
    Film create(Film film);

    //обновление фильма
    Film update(Film film);

    //получение фильма по id
    Film getFilm(int id);

    //получение фильмов
    List<Film> getAllFilms();

    //получение мапы фильмов
    HashMap<Integer, Film> getFilmMap();

    //добавление лайка
    void addLike(int filmId, int userId);

    //удаление лайка
    void deleteLike(int filmId, int userId);

    //получение популярных фильмов
    List<Film> viewPopularFilms(int count);

}
