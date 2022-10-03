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
import ru.practicum.shareit.exceptions.UnsupportedStateException;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private static final String USER_ID = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoOut addBooking(@RequestBody BookingDtoIn booking, @RequestHeader(USER_ID) int userId) {
        log.info("Обработан POST-запрос (/bookings) для пользователя " + userId);
        return bookingService.addBooking(booking, userId);
    }

    @PatchMapping("{bookingId}")
    public BookingDtoOut approveBooking(@RequestHeader(USER_ID) int ownerId, @PathVariable int bookingId,
                                        @RequestParam Boolean approved) {
        log.info("Обработан PATCH-запрос (/bookings/" + bookingId + "?approved=" + approved + ") " +
                "для пользователя " + ownerId);
        return bookingService.approveBooking(ownerId, bookingId, approved);
    }

    @GetMapping("{bookingId}")
    public BookingDtoOut getBookingById(@RequestHeader(USER_ID) int userId,
                                        @PathVariable int bookingId) {
        log.info("Обработан GET-запрос (/bookings/" + bookingId + ") для пользователя " + userId);
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDtoOut> getBookingByBooker(
            @RequestHeader(USER_ID) int bookerId,
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
        return bookingService.getBookingByBooker(bookerId, bookingState,
                PageRequest.of(from / size, size, Sort.by("start").descending()));
    }

    @GetMapping("/owner")
    public List<BookingDtoOut> getBookingByOwner(
            @RequestHeader(USER_ID) int ownerId,
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
        return bookingService.getBookingByOwner(ownerId, bookingState,
                PageRequest.of(from / size, size, Sort.by("start").descending()));
    }
}