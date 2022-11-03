package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.util.Create;
import ru.practicum.shareit.util.Update;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;
    private static final String USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getByOwner(
            @RequestHeader(USER_ID) long userId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
            @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Обработан GET-запрос (/items" + "?from=" + from + "&size=" + size +
                ") для пользователя " + userId);
        return itemClient.getByOwner(userId, from, size);
    }

    @GetMapping("{itemId}")
    public ResponseEntity<Object> getById(@PathVariable long itemId, @RequestHeader(USER_ID) long userId) {
        log.info("Обработан GET-запрос (/items/" + userId + ") для пользователя " + userId);
        return itemClient.getById(itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam(name = "text") String text,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Обработан GET-запрос (/search?text=" + text + "&from=" + from + "&size=" + size + ")");
        return itemClient.search(text, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> create(@Validated(Create.class) @RequestBody ItemDtoIn item,
                                         @RequestHeader(USER_ID) long userId) {
        log.info("Обработан POST-запрос (/items) для пользователя " + userId);
        return itemClient.create(userId, item);
    }

    @PatchMapping("{itemId}")
    public ResponseEntity<Object> update(@Validated(Update.class) @RequestBody ItemDtoIn item,
                                         @PathVariable long itemId, @RequestHeader(USER_ID) long userId) {
        log.info("Обработан PATCH-запрос (/items/" + itemId + ") для пользователя " + userId);
        if (item.getName() != null) {
            if (item.getName().isBlank()) {
                throw new ValidationException("У вещи пустое имя");
            }
        }
        if (item.getDescription() != null) {
            if (item.getDescription().isBlank()) {
                throw new ValidationException("У вещи пустое описание");
            }
        }


        return itemClient.update(userId, item, itemId);
    }

    @DeleteMapping("{itemId}")
    public void delete(@PathVariable long itemId) {
        log.info("Обработан DELETE-запрос (/items/" + itemId + ")");
        itemClient.delete(itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@Valid @RequestBody CommentDtoIn comment,
                                             @RequestHeader(USER_ID) int userId,
                                             @PathVariable int itemId) {
        log.info("Обработан POST-запрос (/items/" + itemId + "/comment) для пользователя " + userId);
        return itemClient.addComment(userId, comment, itemId);
    }
}
