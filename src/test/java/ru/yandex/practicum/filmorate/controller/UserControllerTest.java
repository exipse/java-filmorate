package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userController;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();
    }

    @AfterEach
    public void after() {
        UserController.setCount(1);
    }

    @Test
    public void getUsers() throws ValidationException {
        User user = new User("something@gmail.com", "something",
                LocalDate.of(1990, 01, 01));
        userController.create(user);
        assertEquals(1, userController.getAllUsers().size());
        assertEquals("something", user.getName());
        assertTrue(user.getBirthday().isBefore(LocalDate.now().plusDays(1)));
    }

    @Test
    public void addEmptyEmail() throws ValidationException {
        User user = new User("", "something",
                LocalDate.of(1990, 01, 01));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Электронная почта не может быть пустой", violations.iterator().next().getMessage());
    }

    @Test
    public void addNoCorrectEmail() throws ValidationException {
        User user = new User("test.gmail.com", "something",
                LocalDate.of(1990, 01, 01));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Электронная почта должна содержать символ @", violations.iterator().next().getMessage());
    }

    @Test
    public void addEmptyLogin() throws ValidationException {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        User user = new User("test@gmail.com", "",
                                LocalDate.of(1990, 01, 01));
                        userController.create(user);
                    }
                });
        assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    public void addLoginWithSpace() throws ValidationException {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        User user = new User("test@gmail.com", "test test",
                                LocalDate.of(1990, 01, 01));
                        userController.create(user);
                    }
                });
        assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    public void tryCreateWithBirthdayInFuture() throws ValidationException {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws ValidationException {
                        User user = new User("test@gmail.com", "something",
                                LocalDate.MAX);
                        userController.create(user);
                    }
                });
        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }
}