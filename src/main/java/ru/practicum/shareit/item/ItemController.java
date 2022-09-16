package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.util.Create;
import ru.practicum.shareit.util.Update;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private static final String USER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;

    @GetMapping
    public List<ItemDtoOutWithBooking> getByOwner(@RequestHeader(USER_ID) int userId) {
        log.info("Обработан GET-запрос (/items)");
        return itemService.getByOwner(userId);
    }

    @GetMapping("{itemId}")
    public ItemDtoOutWithBooking getById(@PathVariable int itemId, @RequestHeader(USER_ID) int userId) {
        log.info("Обработан GET-запрос (/items/" + userId + ")");
        return itemService.getById(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDtoOut> search(@RequestParam String text) {
        log.info("Обработан GET-запрос (/search&text=" + text + ")");
        return itemService.search(text);
    }

    @PostMapping
    public ItemDtoOut create(@Validated(Create.class) @RequestBody ItemDtoIn item,
                             @RequestHeader(USER_ID) int userId) {
        log.info("Обработан POST-запрос (/items)");
        return itemService.create(userId, item);
    }

    @PatchMapping("{itemId}")
    public ItemDtoOut update(@Validated(Update.class) @RequestBody ItemDtoIn item, @PathVariable int itemId,
                             @RequestHeader(USER_ID) int userId) {
        log.info("Обработан PATCH-запрос (/items/" + itemId + ")");
        item.setId(itemId);
        return itemService.update(userId, item);
    }

    @DeleteMapping("{itemId}")
    public void delete(@PathVariable int itemId) {
        log.info("Обработан DELETE-запрос (/items/" + itemId + ")");
        itemService.delete(itemId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOut addComment(@Valid @RequestBody CommentDtoIn comment,
                                    @RequestHeader(USER_ID) int userId,
                                    @PathVariable int itemId) {
        log.info("Обработан POST-запрос (/items/" + itemId + "/comment)");
        return itemService.addComment(comment, itemId, userId);
    }
}