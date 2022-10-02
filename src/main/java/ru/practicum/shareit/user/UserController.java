package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.util.Create;
import ru.practicum.shareit.util.Update;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAll(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Обработан GET-запрос (/users&from=)" + from + "&size=" + size + ")");
        return userService.getAll(PageRequest.of(from / size, size, Sort.by("id")));
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
    public void deleteUser(@PathVariable int userId) {
        log.info("Обработан DELETE-запрос (/users/" + userId + ")");
        userService.delete(userId);
    }
}