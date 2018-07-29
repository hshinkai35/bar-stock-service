package api.entitiy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    private Long id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static User create(String name,
                              String email,
                              String password) {
        LocalDateTime now = LocalDateTime.now();
        return new User(null, name, email, password, now, now);
    }

    public void update(String name,
                       String email,
                       String password) {
        setName(name);
        setEmail(email);
        setPassword(password);
        setUpdatedAt(LocalDateTime.now());
    }
}
