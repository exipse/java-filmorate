package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Friend {
    private int userId;
    private int friendId;
    private String status;
}
