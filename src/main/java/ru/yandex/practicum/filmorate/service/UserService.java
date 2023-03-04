package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNoFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class UserService {

    InMemoryUserStorage inMemoryUserStorage;
    private LocalDate today = LocalDate.now();

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User create(User user) {
        validateObject(user);
        inMemoryUserStorage.create(user);
        log.info("Юзер с id = " + user.getId() + " сохранен");
        return user;
    }

    public User update(User user) {
        validateObject(user);
        inMemoryUserStorage.update(user);
        log.info("Данные по юзеру с id = " + user.getId() + " обновлены");
        return user;
    }

    public List<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    public User getUser(int user) {
        return inMemoryUserStorage.getUser(user);
    }


    public void addFriends(int userId, int friendId) {
        ifExistsUsers(userId, friendId);
        User user = inMemoryUserStorage.getMapUsers().get(userId);
        User friend = inMemoryUserStorage.getMapUsers().get(friendId);
        user.addInList(friendId);
        friend.addInList(userId);
        log.info(String.format("%d и %d теперь друзья", userId, friendId));
    }

    public void deletefromFriends(int userId, int friendId) {
        ifExistsUsers(userId, friendId);
        User user = inMemoryUserStorage.getMapUsers().get(userId);
        User friend = inMemoryUserStorage.getMapUsers().get(friendId);
        if (user.getFriendsList().contains(friendId)
                && friend.getFriendsList().contains(userId)) {
            user.getFriends().remove(friendId);
            friend.getFriends().remove(userId);
        }
        log.info(String.format("%d и %d теперь не друзья", userId, friendId));
    }

    public List<User> getFriendsByUser(int userId) {
        return inMemoryUserStorage.getFriendsByUser(userId);
    }

    public List<User> getMatchingFriends(int userId, int friendId) {
        ifExistsUsers(userId, friendId);
        List<User> matchFriends = inMemoryUserStorage.getMatchingFriends(userId, friendId);
        log.info("Список совпадений друзей возвращен");
        return matchFriends;
    }

    private void ifExistsUsers(int userId, int friendId) {
        if (!inMemoryUserStorage.getMapUsers().containsKey(userId)) {
            throw new UserNoFoundException("Юзера с id = " + userId + " не существует");
        }
        if (!inMemoryUserStorage.getMapUsers().containsKey(friendId)) {
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
