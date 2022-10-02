package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDtoIn;
import ru.practicum.shareit.requests.dto.ItemRequestDtoOut;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private static final String USER_ID = "X-Sharer-User-Id";

    public final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDtoOut create(@Valid @RequestBody ItemRequestDtoIn item,
                                    @RequestHeader(USER_ID) int requesterId) {
        log.info("Обработан POST-запрос (/requests)");
        return itemRequestService.create(item, requesterId);
    }

    @GetMapping
    public List<ItemRequestDtoOut> getByRequester(@RequestHeader(USER_ID) int requesterId) {
        log.info("Обработан GET-запрос (/requests)");
        return itemRequestService.getByRequester(requesterId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoOut> getAll(
            @RequestHeader(USER_ID) int userId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
            @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return itemRequestService.getAll(userId,
                PageRequest.of(from / size, size, Sort.by("creationDate")));
    }

    @GetMapping("{requestId}")
    public ItemRequestDtoOut getById(@PathVariable int requestId, @RequestHeader(USER_ID) int userId) {
        return itemRequestService.getById(requestId, userId);
    }
}