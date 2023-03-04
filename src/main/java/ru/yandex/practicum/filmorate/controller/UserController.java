package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        return userService.getUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriends(@PathVariable("id") int userId,
                           @PathVariable int friendId) {
        userService.addFriends(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFromFriends(@PathVariable("id") int userId,
                                  @PathVariable int friendId) {
        userService.deletefromFriends(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendsByUser(@PathVariable("id") int userId) {
        return userService.getFriendsByUser(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMatchingFriends(@PathVariable("id") int userId,
                                         @PathVariable("otherId") int friendId) {
        return userService.getMatchingFriends(userId, friendId);
    }

}
