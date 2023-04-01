package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNoFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private UserStorage userStorage;
    private LocalDate today = LocalDate.now();

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        validateObject(user);
        userStorage.create(user);
        log.info("Юзер с id = " + user.getId() + " сохранен");
        return user;
    }

    public User update(User user) {
        validateObject(user);
        userStorage.update(user);
        log.info("Данные по юзеру с id = " + user.getId() + " обновлены");
        return user;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUser(int user) {
        return userStorage.getUser(user).orElseThrow(() -> new UserNoFoundException("Пользователь не найден"));
    }

    public void addFriends(int userId, int friendId) {
        ifExistsUsers(userId, friendId);
        userStorage.addFriends(userId, friendId);
        log.info(String.format("%d и %d теперь друзья", userId, friendId));
    }

    public void deletefromFriends(int userId, int friendId) {
        ifExistsUsers(userId, friendId);
        userStorage.deletefromFriends(userId, friendId);
        log.info(String.format("%d и %d теперь не друзья", userId, friendId));
    }

    public List<User> getFriendsByUser(int userId) {
        return userStorage.getFriendsByUser(userId);
    }

    public List<User> getMatchingFriends(int userId, int friendId) {
        ifExistsUsers(userId, friendId);
        List<User> matchFriends = userStorage.getMatchingFriends(userId, friendId);
        log.info("Список совпадений друзей возвращен");
        return matchFriends;
    }

    private void ifExistsUsers(int userId, int friendId) {
        if (!userStorage.getUser(userId).isPresent()) {
            throw new UserNoFoundException("Юзера с id = " + userId + " не существует");
        }
        if (!userStorage.getUser(friendId).isPresent()) {
            throw new UserNoFoundException("Юзера с id = " + friendId + " не существует");
        }
    }

    private void validateObject(User user) {
        if (containsWhiteSpace(user.getLogin())) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(today)) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    private boolean containsWhiteSpace(String line) {
        if (line.isBlank()) {
            return true;
        }
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ' ') {
                return true;
            }
        }
        return false;
    }
}
