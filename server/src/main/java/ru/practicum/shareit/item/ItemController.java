package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private static final String USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemDtoOutWithBooking> getByOwner(
            @RequestHeader(USER_ID) int userId,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Обработан GET-запрос (/items" + "?from=" + from + "&size=" + size +
                ") для пользователя " + userId);
        return itemService.getByOwner(userId, PageRequest.of(from / size, size, Sort.by("id")));
    }

    @GetMapping("{itemId}")
    public ItemDtoOutWithBooking getById(@PathVariable int itemId, @RequestHeader(USER_ID) int userId) {
        log.info("Обработан GET-запрос (/items/" + userId + ") для пользователя " + userId);
        return itemService.getById(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDtoOut> search(@RequestParam(name = "text") String text,
                                   @RequestParam(name = "from", defaultValue = "0") int from,
                                   @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Обработан GET-запрос (/search?text=" + text + "&from=" + from + "&size=" + size + ")");
        return itemService.search(text, PageRequest.of(from / size, size, Sort.by("id")));
    }

    @PostMapping
    public ItemDtoOut create(@RequestBody ItemDtoIn item,
                             @RequestHeader(USER_ID) int userId) {
        log.info("Обработан POST-запрос (/items) для пользователя " + userId);
        return itemService.create(userId, item);
    }

    @PatchMapping("{itemId}")
    public ItemDtoOut update(@RequestBody ItemDtoIn item, @PathVariable int itemId,
                             @RequestHeader(USER_ID) int userId) {
        log.info("Обработан PATCH-запрос (/items/" + itemId + ") для пользователя " + userId);
        item.setId(itemId);
        return itemService.update(userId, item);
    }

    @DeleteMapping("{itemId}")
    public void delete(@PathVariable int itemId) {
        log.info("Обработан DELETE-запрос (/items/" + itemId + ")");
        itemService.delete(itemId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOut addComment(@RequestBody CommentDtoIn comment,
                                    @RequestHeader(USER_ID) int userId,
                                    @PathVariable int itemId) {
        log.info("Обработан POST-запрос (/items/" + itemId + "/comment) для пользователя " + userId);
        return itemService.addComment(comment, itemId, userId);
    }
}