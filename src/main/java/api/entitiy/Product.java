package api.entitiy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product {

    private Long id;
    private String name;
    private int numberOfStock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Product create(String name,
                          int numberOfStock) {
        LocalDateTime now = LocalDateTime.now();
        return new Product(null, name, numberOfStock, now, now);
    }
}
