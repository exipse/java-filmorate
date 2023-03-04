package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

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
    User getUser(int userId);

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