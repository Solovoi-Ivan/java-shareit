package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.util.Create;
import ru.practicum.shareit.util.Update;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private static final String USER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;

    @GetMapping
    public List<ItemDtoOutWithBooking> getByOwner(@RequestHeader(USER_ID) int userId) {
        return itemService.getByOwner(userId);
    }

    @GetMapping("{itemId}")
    public ItemDtoOutWithBooking getById(@PathVariable int itemId, @RequestHeader(USER_ID) int userId) {
        return itemService.getById(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDtoOut> search(@RequestParam String text) {
        return itemService.search(text);
    }

    @PostMapping
    public ItemDtoOut create(@Validated(Create.class) @RequestBody ItemDtoIn item,
                             @RequestHeader(USER_ID) int userId) {
        return itemService.create(userId, item);
    }

    @PatchMapping("{itemId}")
    public ItemDtoOut update(@Validated(Update.class) @RequestBody ItemDtoIn item, @PathVariable int itemId,
                             @RequestHeader(USER_ID) int userId) {
        item.setId(itemId);
        return itemService.update(userId, item);
    }

    @DeleteMapping("{itemId}")
    public void delete(@PathVariable int itemId) {
        itemService.delete(itemId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOut addComment(@Valid @RequestBody CommentDtoIn comment,
                                    @RequestHeader(USER_ID) int userId,
                                    @PathVariable int itemId) {
        return itemService.addComment(comment, itemId, userId);
    }
}