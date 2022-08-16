package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserController;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private static final String USER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;
    private final UserController userController;

    @GetMapping
    public List<ItemDto> getByOwner(@RequestHeader(USER_ID) int userId) {
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
    public ItemDto create(@RequestBody ItemDto item,
                          @RequestHeader(USER_ID) int userId) {
        return itemService.create(userController.getById(userId).getId(), item);
    }

    @PatchMapping("{itemId}")
    public ItemDto update(@RequestBody ItemDto item, @PathVariable int itemId,
                          @RequestHeader(USER_ID) int userId) {
        item.setId(itemId);
        return itemService.update(userController.getById(userId).getId(), item);
    }

    @DeleteMapping("{itemId}")
    public void delete(@PathVariable int itemId) {
        itemService.delete(itemId);
    }
}