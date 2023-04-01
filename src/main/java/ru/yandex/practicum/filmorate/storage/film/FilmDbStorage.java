package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNoFoundException;
import ru.yandex.practicum.filmorate.exception.GenreNoFoundException;
import ru.yandex.practicum.filmorate.exception.MPANoFoundException;
import ru.yandex.practicum.filmorate.exception.UserNoFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    Comparator<Genre> comparator = (Genre o1, Genre o2) -> {
        return Integer.compare(o1.getId(), o2.getId());
    };

    @Override
    public Film create(Film film) {
        String sqlQuery = "insert into film (name, description, realease_date, duration, mpa_ID ) values (?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        int filmId = keyHolder.getKey().intValue();
        film.setId(filmId);

        ifGenreExists(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        Optional<Film> filmCheck = getFilm(film.getId());

        String sqlQuery = "update film set " +
                "name = ?, description = ?, realease_date = ?, duration = ?, mpa_ID = ? " +
                "where id = ?";
        jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getMpa().getId()
                , film.getId());

        String sqlQuery1 = "DELETE FROM film_genre where film_id = ?";
        jdbcTemplate.update(sqlQuery1, film.getId());

        Film validateFilm = ifGenreExists(film);
        return validateFilm;
    }

    @Override
    public Optional<Film> getFilm(int id) {
        String sqlQuery = "select * from film where id = ?";
        Optional<Film> film;
        try {
            film = Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::rowToFilm, id));
        } catch (EmptyResultDataAccessException e) {
            throw new FilmNoFoundException("Фильм по ID не найден");
        }
        return film;
    }

    @Override
    public List<Film> getAllFilms() {

        String sqlQuery = "select * from film";
        return jdbcTemplate.query(sqlQuery, this::rowToFilm);
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sql = "select * from user_like where film_id = ? and user_id = ?";
        SqlRowSet existLike = jdbcTemplate.queryForRowSet(sql, filmId, userId);
        if (!existLike.next()) {
            String sqlQuery = "INSERT INTO user_like (film_id, user_id) VALUES (?,?)";
            jdbcTemplate.update(sqlQuery, filmId, userId);
        }
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        String sqlQuery = "DELETE FROM user_like WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public List<Film> viewPopularFilms(int count) {
        String sqlQuery = "SELECT count(ul.user_id) as morelike, f.id, f.name, f.description, f.realease_date, f.duration, f.mpa_ID, r.id, r.name, r.description from film f\n" +
                "left join user_like ul on f.id = ul.film_id\n" +
                "inner join mpa r on f.mpa_ID = r.id\n" +
                "group by f.id\n" +
                "order by morelike desc\n" +
                "limit ?";
        return jdbcTemplate.query(sqlQuery, this::rowToFilm, count);
    }

    @Override
    public List<Genre> getGenreList() {
        String sqlQuery = "SELECT * FROM genre";
        return jdbcTemplate.query(sqlQuery, this::rowToGenre);
    }

    @Override
    public Genre getGenreById(Integer id) {
        String sqlQuery = "SELECT * FROM genre where id = ?";
        Genre genre;
        try {
            genre = jdbcTemplate.queryForObject(sqlQuery, this::rowToGenre, id);
        } catch (EmptyResultDataAccessException e) {
            throw new GenreNoFoundException("Жанр с таким id не найден");
        }
        return genre;
    }


    @Override
    public List<MPA> getMpaList() {
        String sqlQuery = "SELECT * FROM mpa";
        return jdbcTemplate.query(sqlQuery, this::rowToMpa);
    }

    @Override
    public MPA getMpaById(Integer id) {
        String sqlQuery = "SELECT * FROM mpa where id = ?";
        MPA mpa;
        try {
            mpa = jdbcTemplate.queryForObject(sqlQuery, this::rowToMpa, id);
        } catch (EmptyResultDataAccessException e) {
            throw new MPANoFoundException("Рейтинг с таким id не найден");
        }
        return mpa;
    }


    private Film ifGenreExists(Film film) {
        List<Genre> genre = film.getGenres();
        if (!(genre == null)) {
            Set<Genre> genreSet = genre.stream().collect(Collectors.toSet());
            List<Genre> genreWhithOutDuble = genreSet.stream().collect(Collectors.toList());
            Collections.sort(genreWhithOutDuble, comparator);
            for (Genre genreN1 : genreSet) {
                String sql = "insert into film_genre (film_id, genre_id) values (?, ?)";
                jdbcTemplate.update(sql, film.getId(), genreN1.getId());
            }
            film.setGenres(genreWhithOutDuble);
        }
        return film;
    }

    private Film rowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        int filmId = resultSet.getInt("id");
        return Film.builder()
                .id(filmId)
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("realease_date").toLocalDate())
                .duration(resultSet.getLong("duration"))
                .mpa(
                        getMpaById(resultSet.getInt("MPA_ID"))
                )
                .genres(getGenresFilm(filmId))
                .build();
    }

    private List<Genre> getGenresFilm(Integer filmid) {
        String sqlQuery = "SELECT id, name FROM genre g " +
                "inner join film_genre fg on g.id = fg.genre_id where fg.film_id = ?";
        return jdbcTemplate.query(sqlQuery, this::rowToGenre, filmid);
    }

    private Genre rowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }

    private MPA rowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return MPA.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .build();
    }

}
