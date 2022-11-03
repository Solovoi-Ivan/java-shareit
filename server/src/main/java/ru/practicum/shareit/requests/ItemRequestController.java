package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDtoIn;
import ru.practicum.shareit.requests.dto.ItemRequestDtoOut;

import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private static final String USER_ID = "X-Sharer-User-Id";

    public final ItemRequestService itemRequestService;

    @GetMapping
    public List<ItemRequestDtoOut> getByRequester(@RequestHeader(USER_ID) int requesterId) {
        log.info("Обработан GET-запрос (/requests) для пользователя " + requesterId);
        return itemRequestService.getByRequester(requesterId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoOut> getAll(
            @RequestHeader(USER_ID) int userId,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Обработан GET-запрос (/requests/all" + "?from=" + from + "&size=" + size +
                ") для пользователя " + userId);
        return itemRequestService.getAll(userId,
                PageRequest.of(from / size, size, Sort.by("creationDate")));
    }

    @GetMapping("{requestId}")
    public ItemRequestDtoOut getById(@PathVariable int requestId, @RequestHeader(USER_ID) int userId) {
        log.info("Обработан GET-запрос (/requests/" + requestId + ") для пользователя " + userId);
        return itemRequestService.getById(requestId, userId);
    }

    @PostMapping
    public ItemRequestDtoOut create(@RequestBody ItemRequestDtoIn itemRequest,
                                    @RequestHeader(USER_ID) int requesterId) {
        log.info("Обработан POST-запрос (/requests) для пользователя " + requesterId);
        return itemRequestService.create(itemRequest, requesterId);
    }
}