package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    /**
     * Сохранение пользователя
     */
    User create(User user);

    /**
     * Обновление пользователя
     */
    User update(User user);

    /**
     * Получение информации о пользователе
     */
    Optional<User> getUser(int userId);

    /**
     * Добавление в список друзей
     */
    void addFriends(int userId, int friendId);

    /**
     * Удаление из списка друзей
     */
    void deletefromFriends(int userId, int friendId);

    /**
     * Получение пользователей
     */
    List<User> getAllUsers();

    /**
     * Получение друзей пользователя
     */
    List<User> getFriendsByUser(int userId);

    /**
     * Получение общих друзей
     */
    List<User> getMatchingFriends(int userId, int friendId);

}