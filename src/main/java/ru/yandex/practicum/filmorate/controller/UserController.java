package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private static int count = 1;
    private HashMap<Integer, User> mapUsers = new HashMap<>();
    private LocalDate today = LocalDate.now();

    public static void setCount(int count) {
        UserController.count = count;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        validateObject(user);
        user.setId(count);
        count += 1;
        mapUsers.put(user.getId(), user);
        log.info("Юзер с id = " + user.getId() + " сохранен");
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        validateObject(user);
        if (!mapUsers.containsKey(user.getId())) {
            log.error("Юзера с id = " + user.getId() + " не существует");
            throw new ValidationException("Юзера с id = " + user.getId() + " не существует");
        }
        mapUsers.put(user.getId(), user);
        log.info("Данные по юзеру с id = " + user.getId() + " обновлены");
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(mapUsers.values());
    }

    private void validateObject(User user) {
        if (containsWhiteSpace(user.getLogin())) {
            log.error("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(today)) {
            log.error("Дата рождения не может быть в будущем");
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
