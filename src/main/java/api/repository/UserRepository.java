package api.repository;

import api.entitiy.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder encoder;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate, PasswordEncoder encoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.encoder = encoder;
    }

    public List<User> findAll() {
        RowMapper<User> mapper = new BeanPropertyRowMapper<>(User.class);
        return jdbcTemplate.query("SELECT * FROM `users`;", mapper);
    }

    public Optional<User> findById(Long id) {
        List<User> result = jdbcTemplate.query(
                "SELECT * FROM `users` WHERE `id` = ?;",
                (rs, rowNum) -> new User(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getTimestamp(5).toLocalDateTime(),
                        rs.getTimestamp(6).toLocalDateTime()), id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public Optional<User> findByEmail(String email) {
        List<User> result = jdbcTemplate.query("SELECT * FROM `users` WHERE `email` = ?;",
                (rs, rowNum) -> new User(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getTimestamp(5).toLocalDateTime(),
                        rs.getTimestamp(6).toLocalDateTime()), email);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public void save(User user) {
        if (user.getId() == null) {
            create(user);
        } else {
            update(user);
        }
    }

    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM `users` WHERE `id` = ?;", id);
    }

    private void create(User user) {
        jdbcTemplate.update(
                "INSERT INTO `users` (`name`, `email`, `password`, `created_at`, `updated_at`) VALUES (?, ?, ?, ?, ?);",
                user.getName(), user.getEmail(), encoder.encode(user.getPassword()), user.getCreatedAt(), user.getUpdatedAt());
    }

    private void update(User user) {
        jdbcTemplate.update(
                "UPDATE `users` SET `name`= ?, `email`= ?, `password`= ?,`updated_at` = ? WHERE `id` = ?;",
                user.getName(), user.getEmail(), encoder.encode(user.getPassword()), user.getUpdatedAt(), user.getId());
    }

}
