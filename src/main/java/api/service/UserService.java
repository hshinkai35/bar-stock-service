package api.service;

import api.dto.UserDto;
import api.entitiy.User;
import api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository repository;

    public List<User> getUsers() {
        return repository.findAll();
    }

    public User getUser(Long id) {
        Optional<User> optionalUser = repository.findById(id);
        return optionalUser.orElse(null);
    }

    public void create(UserDto dto) {
        repository.save(User.create(dto.getName(), dto.getEmail(), dto.getPassword()));
    }

    public void update(long id, UserDto dto) {
        Optional<User> optionalUser = repository.findById(id);
        optionalUser.ifPresentOrElse(user -> {
            user.update(dto.getName(), dto.getEmail(), dto.getPassword());
            repository.save(user);
        }, () -> {
            throw new RuntimeException("user not found");
        });
    }

    public void deleteUser(long id) {
        repository.delete(id);
    }


}
