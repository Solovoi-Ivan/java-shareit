package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final UserController userController;

    @GetMapping
    public List<ItemDto> getByOwner(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getByOwner(userId);
    }

    @GetMapping("{itemId}")
    public ItemDto getById(@PathVariable int itemId) {
        return itemService.getById(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }

    @PostMapping
    public ItemDto create(@Valid @RequestBody ItemDto item,
                          @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.create(userController.getById(userId).getId(), item);
    }

    @PatchMapping("{itemId}")
    public ItemDto update(@Valid @RequestBody ItemDto item, @PathVariable int itemId,
                          @RequestHeader("X-Sharer-User-Id") int userId) {
        item.setId(itemId);
        return itemService.update(userController.getById(userId).getId(), item);
    }

    @DeleteMapping("{itemId}")
    public void delete(@PathVariable int itemId) {
        itemService.delete(itemId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleFailedFieldsValidation(final ValidationException e) {
        return Map.of("error", "NOT VALID", "message", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundValidation(final NotFoundException e) {
        return Map.of("error", "NOT VALID", "message", e.getMessage());
    }
}
