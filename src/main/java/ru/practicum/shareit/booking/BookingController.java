package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.util.BookingState;
import ru.practicum.shareit.exceptions.UnsupportedStateException;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private static final String USER_ID = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoOut addBooking(@RequestBody BookingDtoIn booking, @RequestHeader(USER_ID) int userId) {
        return bookingService.addBooking(booking, userId);
    }

    @PatchMapping("{bookingId}")
    public BookingDtoOut approveBooking(@RequestHeader(USER_ID) int ownerId, @PathVariable int bookingId,
                                        @RequestParam Boolean approved) {
        return bookingService.approveBooking(ownerId, bookingId, approved);
    }

    @GetMapping("{bookingId}")
    public BookingDtoOut getBookingById(@RequestHeader(USER_ID) int userId,
                                        @PathVariable int bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDtoOut> getBookingByBooker(@RequestHeader(USER_ID) int bookerId,
                                                  @RequestParam(defaultValue = "ALL") String state) {
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStateException("Unknown state: " + state.toUpperCase());
        }
        return bookingService.getBookingByBooker(bookerId, bookingState);
    }

    @GetMapping("/owner")
    public List<BookingDtoOut> getBookingByOwner(@RequestHeader(USER_ID) int ownerId,
                                                 @RequestParam(defaultValue = "ALL") String state) {
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStateException("Unknown state: " + state.toUpperCase());
        }
        return bookingService.getBookingByOwner(ownerId, bookingState);
    }
}