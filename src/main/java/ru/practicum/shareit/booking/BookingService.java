package ru.practicum.shareit.booking;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.util.BookingState;

import java.util.List;

public interface BookingService {
    BookingDtoOut addBooking(BookingDtoIn booking, int userId);

    BookingDtoOut approveBooking(int ownerId, int bookingId, Boolean isApproved);

    BookingDtoOut getBookingById(int userId, int bookingId);

    List<BookingDtoOut> getBookingByBooker(int bookerId, BookingState state, PageRequest pageRequest);

    List<BookingDtoOut> getBookingByOwner(int ownerId, BookingState state, PageRequest pageRequest);
}
