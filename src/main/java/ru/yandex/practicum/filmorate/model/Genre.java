package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@AllArgsConstructor
public class Genre {
    @NonNull
    private int id;
    private String name;

    public Genre(int id) {
        this.id = id;
    }
}
