package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private int id;
    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Электронная почта должна содержать символ @")
    @NonNull
    private String email;
    @NonNull
    private String login;
    private String name;
    @NonNull
    private LocalDate birthday;
    private Set<Integer> friends;

    public void addInList(int idFriend) {
        if (friends == null) {
            friends = new HashSet<>();
        }
        friends.add(idFriend);
    }

    public Set<Integer> getFriendsList() {
        if (friends == null) {
            friends = new HashSet<>();
        }
        return friends;
    }
}
