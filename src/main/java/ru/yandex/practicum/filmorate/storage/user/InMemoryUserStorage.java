package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNoFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("InMemoryUserStorage")
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
    public Optional<User> getUser(int userId) {
        if (!mapUsers.containsKey(userId)) {
            throw new UserNoFoundException("Юзера с id = " + userId + " не существует");
        }
        return Optional.of(mapUsers.get(userId));
    }

    @Override
    public void addFriends(int userId, int friendId) {
        Optional<User> userOpt = getUser(userId);
        Optional<User> friendOpt = getUser(friendId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.addInList(user);
        }
        if (friendOpt.isPresent()) {
            User friend = friendOpt.get();
            friend.addInList(friend);
        }
    }

    @Override
    public void deletefromFriends(int userId, int friendId) {
        Optional<User> userOpt = getUser(userId);
        Optional<User> friendOpt = getUser(friendId);
        if (userOpt.isPresent() && friendOpt.isPresent()) {
            User user = userOpt.get();
            User friend = friendOpt.get();
            if (user.getFriends().contains(user)
                    && friend.getFriends().contains(friend)) {
                user.getFriends().remove(friend);
                friend.getFriends().remove(user);
            }
        }
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
        List<User> friendList = getMapUsers().get(userId).getFriends();
        return friendList;
    }

    @Override
    public List<User> getMatchingFriends(int userId, int friendId) {
        List<User> userFriends = getMapUsers().get(userId).getFriends();
        List<User> friendFriends = getMapUsers().get(friendId).getFriends();
        List<User> commonFriends = userFriends.stream().filter(friendFriends::contains).collect(Collectors.toList());
        return commonFriends;
    }
}

