package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNoFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {

    private static int count = 1;
    private HashMap<Integer, User> mapUsers = new HashMap<>();

    public HashMap<Integer, User> getMapUsers() {
        return mapUsers;
    }

    public static void setCount(int count) {
        InMemoryUserStorage.count = count;
    }

    @Override
    public User create(User user) {
        user.setId(count);
        count += 1;
        mapUsers.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!mapUsers.containsKey(user.getId())) {
            throw new UserNoFoundException("Юзера с id = " + user.getId() + " не существует");
        }
        mapUsers.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUser(int userId) {
        if (!mapUsers.containsKey(userId)) {
            throw new UserNoFoundException("Юзера с id = " + userId + " не существует");
        }
        return mapUsers.get(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(mapUsers.values());
    }

    @Override
    public List<User> getFriendsByUser(int userId) {
        if (!getMapUsers().containsKey(userId)) {
            throw new UserNoFoundException("Юзера с id = " + userId + " не существует");
        }
        List<User> users = new ArrayList<>();
        Set<Integer> friendList = getMapUsers().get(userId).getFriendsList();
        for (Integer integer : friendList) {
            users.add(getMapUsers().get(integer));
        }
        return users;
    }

    @Override
    public List<User> getMatchingFriends(int userId, int friendId) {
        List<User> friends = new ArrayList<>();
        Set<Integer> userFriends = getMapUsers().get(userId).getFriendsList();
        Set<Integer> friendFriends = getMapUsers().get(friendId).getFriendsList();
        Set<Integer> commonFriends = userFriends.stream().filter(friendFriends::contains).collect(Collectors.toSet());
        if (!commonFriends.isEmpty()) {
            for (Integer commonFriend : commonFriends) {
                friends.add(getMapUsers().get(commonFriend));
            }
        }
        return friends;
    }
}

