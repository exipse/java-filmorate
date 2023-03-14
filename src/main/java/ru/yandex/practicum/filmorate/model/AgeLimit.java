package ru.yandex.practicum.filmorate.model;


import lombok.Data;
import lombok.NonNull;

@Data
public class AgeLimit {
    @NonNull
    private int id;
    @NonNull
    private String name;
    private String description;
}
