package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.util.Create;
import ru.practicum.shareit.util.Update;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable int userId) {
        return userService.getById(userId);
    }

    @PostMapping
    public UserDto create(@Validated(Create.class) @RequestBody UserDto user) {
        return userService.create(user);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@Validated(Update.class) @RequestBody UserDto user, @PathVariable int userId) {
        user.setId(getById(userId).getId());
        return userService.update(user);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable int userId) {
        userService.delete(userId);
    }
}