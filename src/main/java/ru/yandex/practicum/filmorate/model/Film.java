package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private int id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @NonNull
    private long duration;
    private Set<Integer> listUsersLikes = new HashSet<>();

    public void addInLikeList(int userId) {
        listUsersLikes.add(userId);
    }

    public Set<Integer> getLikesList() {
        return listUsersLikes;
    }
}
