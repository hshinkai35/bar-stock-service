package api.restcontroller;

import api.dto.UserDto;
import api.entitiy.User;
import api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserRestController {

    private final UserService userService;

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping("/users")
    public void createUser(@RequestBody UserDto dto) {
        userService.create(dto);
    }

    @PutMapping("/users/{id}")
    public void update(@PathVariable Long id, @RequestBody UserDto dto) {
        userService.update(id, dto);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}