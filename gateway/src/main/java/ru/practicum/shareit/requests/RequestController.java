package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDtoIn;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class RequestController {
    private final RequestClient requestClient;
    private static final String USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getByRequester(@RequestHeader(USER_ID) long requesterId) {
        log.info("Обработан GET-запрос (/requests) для пользователя " + requesterId);
        return requestClient.getByRequester(requesterId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(
            @RequestHeader(USER_ID) long userId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
            @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Обработан GET-запрос (/requests/all" + "?from=" + from + "&size=" + size +
                ") для пользователя " + userId);
        return requestClient.getAll(userId, from, size);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> getById(@PathVariable long requestId, @RequestHeader(USER_ID) long userId) {
        log.info("Обработан GET-запрос (/requests/" + requestId + ") для пользователя " + userId);
        return requestClient.getById(requestId, userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ItemRequestDtoIn itemRequest,
                                         @RequestHeader(USER_ID) long requesterId) {
        log.info("Обработан POST-запрос (/requests) для пользователя " + requesterId);
        return requestClient.create(requesterId, itemRequest);
    }
}