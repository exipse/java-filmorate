package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    //сохранение пользователя
    User create(User user);

    //обновление пользователя
    User update(User user);

    //получение информации о пользователе
    User getUser(int userId);

    //получение пользователей
    List<User> getAllUsers();

    //получение друзей пользователя
    List<User> getFriendsByUser(int userId);

    //получение общих друзей
    List<User> getMatchingFriends(int userId, int friendId);

}