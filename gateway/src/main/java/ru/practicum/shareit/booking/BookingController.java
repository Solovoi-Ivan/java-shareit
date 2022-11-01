package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.exceptions.UnsupportedStateException;
import ru.practicum.shareit.util.BookingState;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient bookingClient;
    private static final String USER_ID = "X-Sharer-User-Id";

    @GetMapping("{bookingId}")
    public ResponseEntity<Object> getById(@RequestHeader(USER_ID) long userId,
                                          @PathVariable long bookingId) {
        log.info("Обработан GET-запрос (/bookings/" + bookingId + ") для пользователя " + userId);
        return bookingClient.getById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getByBooker(
            @RequestHeader(USER_ID) long bookerId,
            @RequestParam(defaultValue = "ALL") String state,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
            @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Обработан GET-запрос (/bookings&state=" + state +
                "&from=" + from + "&size=" + size + ") для пользователя " + bookerId);
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStateException("Unknown state: " + state.toUpperCase());
        }
        return bookingClient.getByBooker(bookerId, bookingState, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getByOwner(
            @RequestHeader(USER_ID) long ownerId,
            @RequestParam(defaultValue = "ALL") String state,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
            @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Обработан GET-запрос (/bookings/owner&state=" + state +
                "&from=" + from + "&size=" + size + ") для пользователя " + ownerId);
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStateException("Unknown state: " + state.toUpperCase());
        }
        return bookingClient.getByOwner(ownerId, bookingState, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody BookingDtoIn booking, @RequestHeader(USER_ID) long userId) {
        log.info("Обработан POST-запрос (/bookings) для пользователя " + userId);
        return bookingClient.create(userId, booking);
    }

    @PatchMapping("{bookingId}")
    public ResponseEntity<Object> approve(@RequestHeader(USER_ID) long ownerId, @PathVariable long bookingId,
                                          @RequestParam(name = "approved") Boolean approved) {
        log.info("Обработан PATCH-запрос (/bookings/" + bookingId + "?approved=" + approved + ") " +
                "для пользователя " + ownerId);
        return bookingClient.approve(ownerId, bookingId, approved);
    }
}
