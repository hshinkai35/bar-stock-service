package api.repository;

import api.entitiy.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Product> findAll() {
        RowMapper<Product> mapper = new BeanPropertyRowMapper<>(Product.class);
        return jdbcTemplate.query("SELECT * FROM `products`;", mapper);
    }

    public Optional<Product> findById(Long id) {
        List<Product> result = jdbcTemplate.query("SELECT * FROM `products` WHERE `id` = ?;",
                (rs, rowNum) -> new Product(
                        rs.getLong(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getTimestamp(4).toLocalDateTime(),
                        rs.getTimestamp(5).toLocalDateTime()), id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public void create(Product product) {
        jdbcTemplate.update("INSERT INTO `products` (`name`, `number_of_stock`, `created_at`, `updated_at`) VALUES (?, ?, ?, ?);",
                product.getName(), product.getNumberOfStock(), product.getCreatedAt(), product.getUpdatedAt());
    }

    public void updateNumberOfStock(long id, int numberOfStock) {
        jdbcTemplate.update("UPDATE `products` SET `number_of_stock`= ?, `updated_at` = now() WHERE `id` = ?;",
                numberOfStock, id);
    }

    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM `products` WHERE `id` = ?;", id);
    }
}
