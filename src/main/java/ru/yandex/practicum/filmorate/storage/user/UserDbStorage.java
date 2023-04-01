package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNoFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        String sqlQuery = "insert into users (email, login, name, birthday) values (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        int id = keyHolder.getKey().intValue();
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        Optional<User> findUser = getUser(user.getId());
        String sqlQuery = "update users set " +
                "email = ?, login = ?, name = ?, birthday = ? " +
                "where id = ?";
        jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , user.getId());

        return user;
    }

    @Override
    public Optional<User> getUser(int userId) {
        String sqlQuery = "select * from users where id = ?";
        Optional<User> user;
        try {
            user = Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::rowToUser, userId));
        } catch (EmptyResultDataAccessException e) {
            throw new UserNoFoundException("Пользователь по ID не найден");
        }
        return user;
    }

    @Override
    public void addFriends(int userId, int friendId) {
        String sqlQuery = "insert into friends (user_id, friend_id, status) values (?,?, 'true')";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public void deletefromFriends(int userId, int friendId) {
        String sqlQuery = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "select * from users";
        return jdbcTemplate.query(sqlQuery, this::rowToUser);
    }

    @Override
    public List<User> getFriendsByUser(int userId) {
        String sqlQuery = "select * from users where id in (select friend_id from friends where user_id = ?)";
        return jdbcTemplate.query(sqlQuery, this::rowToUser, userId);
    }

    @Override
    public List<User> getMatchingFriends(int userId, int friendId) {
        String sqlQuery = "select * from users where id in " +
                "(select friend_id from friends where user_id = ? INTERSECT select friend_id from friends where user_id = ?)";
        return jdbcTemplate.query(sqlQuery, this::rowToUser, userId, friendId);
    }

    private User rowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        int userId = resultSet.getInt("id");
        return User.builder()
                .id(resultSet.getInt("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .friends(getFriendsByUser(userId))
                .build();
    }
}
