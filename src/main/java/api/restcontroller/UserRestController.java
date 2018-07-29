package api.restcontroller;

import api.dto.UserDto;
import api.entitiy.User;
import api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserRestController {

    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createUser(@RequestBody UserDto dto) {
        userService.create(dto);
        return "{}";
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String update(@PathVariable Long id, @RequestBody UserDto dto) {
        userService.update(id, dto);
        return "{}";
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}