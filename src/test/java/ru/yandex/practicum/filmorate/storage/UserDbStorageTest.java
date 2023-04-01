package ru.yandex.practicum.filmorate.storage;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {

    private final UserDbStorage userDbStorage;

    @Test
    public void createAndGetUsers() {
        int count = userDbStorage.getAllUsers().size();
        User user1 = User.builder()
                .id(1)
                .email("vasia@mail.ru")
                .login("Vasia07")
                .name("Вася")
                .birthday(LocalDate.of(1990, 01, 01))
                .build();

        User user2 = User.builder()
                .id(1)
                .email("petia@mail.ru")
                .login("petya666")
                .name("Петя")
                .birthday(LocalDate.of(1983, 03, 23))
                .build();

        userDbStorage.create(user1);
        userDbStorage.create(user2);
        assertEquals(count + 2, userDbStorage.getAllUsers().size());
    }

    @Test
    public void updateUser() {
        int count = userDbStorage.getAllUsers().size();
        User user = User.builder()
                .email("vasia@mail.ru")
                .login("Vasia07")
                .name("Вася")
                .birthday(LocalDate.of(1990, 01, 01))
                .build();
        userDbStorage.create(user);

        User userUpd = User.builder()
                .id(count + 1)
                .email("TEST@mail.ru")
                .login("TEST")
                .name("TEST")
                .birthday(LocalDate.of(1990, 01, 01))
                .build();

        userDbStorage.create(user);
        userDbStorage.update(userUpd);
        Optional<User> userInDB = userDbStorage.getUser(count + 1);
        User userInObj = userInDB.get();
        assertEquals(userUpd.getName(), userInObj.getName());
    }


}

