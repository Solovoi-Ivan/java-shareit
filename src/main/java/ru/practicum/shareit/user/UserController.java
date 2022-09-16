package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.util.Create;
import ru.practicum.shareit.util.Update;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAll() {
        log.info("Обработан GET-запрос (/users)");
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable int userId) {
        log.info("Обработан GET-запрос (/users/" + userId + ")");
        return userService.getById(userId);
    }

    @PostMapping
    public UserDto create(@Validated(Create.class) @RequestBody UserDto user) {
        log.info("Обработан POST-запрос (/users)");
        return userService.create(user);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@Validated(Update.class) @RequestBody UserDto user, @PathVariable int userId) {
        log.info("Обработан PATCH-запрос (/users/" + userId + ")");
        user.setId(getById(userId).getId());
        return userService.update(user);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable int userId) {
        log.info("Обработан DELETE-запрос (/users/" + userId + ")");
        userService.delete(userId);
    }
}