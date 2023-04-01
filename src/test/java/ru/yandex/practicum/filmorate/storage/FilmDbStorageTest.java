package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    @Test
    public void createAndGetFilms() {
        int count = filmDbStorage.getAllFilms().size();
        Film firstFilm = Film.builder()
                .id(1)
                .name("Фильм 1")
                .description("о гонках")
                .releaseDate(LocalDate.now().minusYears(23))
                .duration(130)
                .mpa(filmDbStorage.getMpaById(1))
                .listUsersLikes(new HashSet<>())
                .genres(new ArrayList<>())
                .build();

        Film secondFilm = Film.builder()
                .id(1)
                .name("Фильм 2")
                .description("Боевик")
                .releaseDate(LocalDate.now().minusYears(20))
                .duration(130)
                .mpa(filmDbStorage.getMpaById(1))
                .listUsersLikes(new HashSet<>())
                .genres(new ArrayList<>())
                .build();
        filmDbStorage.create(firstFilm);
        filmDbStorage.create(secondFilm);
        assertEquals(count + 2, filmDbStorage.getAllFilms().size());
    }

    @Test
    void addLikeAndViewPopularfilms() {
        int countFilm = filmDbStorage.getAllFilms().size();
        int countUser = userDbStorage.getAllUsers().size();
        Film film = Film.builder()
                .name("Фильм 1")
                .description("о гонках")
                .releaseDate(LocalDate.now().minusYears(23))
                .duration(130)
                .mpa(filmDbStorage.getMpaById(1))
                .listUsersLikes(new HashSet<>())
                .genres(new ArrayList<>())
                .build();

        User user = User.builder()
                .id(1)
                .email("vasia@mail.ru")
                .login("Vasia07")
                .name("Вася")
                .birthday(LocalDate.of(1990, 01, 01))
                .build();
        filmDbStorage.create(film);
        userDbStorage.create(user);
        Optional<User> userInDB = userDbStorage.getUser(countUser + 1);
        User userInObj = userInDB.get();
        filmDbStorage.addLike(countFilm + 1, userInObj.getId());
        List<Film> popularFilm = filmDbStorage.viewPopularFilms(1);
        assertEquals(1, popularFilm.size());
    }


    @Test
    void getGenres() {
        List<Genre> genres = filmDbStorage.getGenreList();
        assertEquals(6, genres.size());
    }

    @Test
    void getMPA() {
        List<MPA> genres = filmDbStorage.getMpaList();
        assertEquals(5, genres.size());
    }
}
