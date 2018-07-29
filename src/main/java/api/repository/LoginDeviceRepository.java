package api.repository;

import api.entitiy.LoginDevice;
import api.entitiy.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class LoginDeviceRepository {

    private final JdbcTemplate jdbcTemplate;

    public LoginDeviceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<LoginDevice> findByUserIdAndDevice(Long userId, String device, String userAgent) {
        RowMapper<LoginDevice> mapper = new BeanPropertyRowMapper<>(LoginDevice.class);
        List<LoginDevice> results = jdbcTemplate.query(
                "SELECT * FROM `login_device` WHERE `user_id` = ? AND `device` = ? AND `user_agent` = ?;", mapper,
                userId, device, userAgent);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public void create(LoginDevice loginDevice) {
        jdbcTemplate.update("INSERT INTO `login_device` (`user_id`, `device`, `user_agent`) VALUES (?, ?, ?);",
                loginDevice.getUserId(), loginDevice.getDevice(), loginDevice.getUserAgent());
    }

    public void delete(Long userId, String device) {
        jdbcTemplate.update("DELETE FROM `login_device` WHERE `user_id` = ? AND `device` = ?;",
                userId, device);
    }
}
