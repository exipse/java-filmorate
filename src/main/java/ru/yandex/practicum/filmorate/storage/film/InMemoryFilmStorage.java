package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNoFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private static int count = 1;
    private final HashMap<Integer, Film> filmMap = new HashMap<>();


    public HashMap<Integer, Film> getFilmMap() {
        return filmMap;
    }

    @Override
    public Film create(Film film) {
        film.setId(count);
        count += 1;
        filmMap.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!filmMap.containsKey(film.getId())) {
            throw new FilmNoFoundException("Фильма с id = " + film.getId() + " не существует");
        }
        filmMap.put(film.getId(), film);
        return film;
    }

    @Override
    public Optional<Film> getFilm(int id) {
        if (!filmMap.containsKey(id)) {
            throw new FilmNoFoundException("Фильма с id = " + id + " не существует");
        }
        return Optional.of(filmMap.get(id));
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmMap.values());
    }


    @Override
    public void addLike(int filmId, int userId) {
        Film film = getFilmMap().get(filmId);
        if (film.getListUsersLikes().contains(userId)) {
            throw new ValidationException("Пользователь с id = " + userId + " ставил like фильму c filmId = " + filmId);
        }
        film.addInLikeList(userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        Film film = getFilmMap().get(filmId);
        if (film.getListUsersLikes().contains(userId)) {
            film.getListUsersLikes().remove(userId);
        } else {
            throw new ValidationException("Пользователь с id = " + userId + " не ставил like фильму c Id = " + filmId);
        }
    }

    @Override
    public List<Film> viewPopularFilms(int count) {
        return getAllFilms().stream()
                .filter(x -> x.getListUsersLikes() != null)
                .sorted((Film o1, Film o2) -> (o2.getListUsersLikes().size() - o1.getListUsersLikes().size()))
                .limit(count).collect(Collectors.toList());
    }

    @Override
    public List<Genre> getGenreList() {
        return null;
    }

    @Override
    public Genre getGenreById(Integer id) {
        return null;
    }

    @Override
    public List<MPA> getMpaList() {
        return null;
    }

    @Override
    public MPA getMpaById(Integer id) {
        return null;
    }

}
