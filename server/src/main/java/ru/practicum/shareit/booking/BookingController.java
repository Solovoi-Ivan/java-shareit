package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.util.BookingState;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private static final String USER_ID = "X-Sharer-User-Id";

    @GetMapping("{bookingId}")
    public BookingDtoOut getById(@RequestHeader(USER_ID) int userId,
                                 @PathVariable int bookingId) {
        log.info("Обработан GET-запрос (/bookings/" + bookingId + ") для пользователя " + userId);
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDtoOut> getByBooker(
            @RequestHeader(USER_ID) int bookerId,
            @RequestParam(defaultValue = "ALL") String state,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Обработан GET-запрос (/bookings&state=" + state +
                "&from=" + from + "&size=" + size + ") для пользователя " + bookerId);
        return bookingService.getByBooker(bookerId, BookingState.valueOf(state.toUpperCase()),
                PageRequest.of(from / size, size, Sort.by("start").descending()));
    }

    @GetMapping("/owner")
    public List<BookingDtoOut> getByOwner(
            @RequestHeader(USER_ID) int ownerId,
            @RequestParam(defaultValue = "ALL") String state,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Обработан GET-запрос (/bookings/owner&state=" + state +
                "&from=" + from + "&size=" + size + ") для пользователя " + ownerId);
        return bookingService.getByOwner(ownerId, BookingState.valueOf(state.toUpperCase()),
                PageRequest.of(from / size, size, Sort.by("start").descending()));
    }

    @PostMapping
    public BookingDtoOut create(@RequestBody BookingDtoIn booking, @RequestHeader(USER_ID) int userId) {
        log.info("Обработан POST-запрос (/bookings) для пользователя " + userId);
        return bookingService.create(booking, userId);
    }

    @PatchMapping("{bookingId}")
    public BookingDtoOut approve(@RequestHeader(USER_ID) int ownerId, @PathVariable int bookingId,
                                 @RequestParam(name = "approved") Boolean approved) {
        log.info("Обработан PATCH-запрос (/bookings/" + bookingId + "?approved=" + approved + ") " +
                "для пользователя " + ownerId);
        return bookingService.approve(ownerId, bookingId, approved);
    }
}