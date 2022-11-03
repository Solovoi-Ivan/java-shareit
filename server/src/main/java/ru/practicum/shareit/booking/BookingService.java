package ru.practicum.shareit.booking;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.util.BookingState;

import java.util.List;

public interface BookingService {
    BookingDtoOut create(BookingDtoIn booking, int userId);

    BookingDtoOut approve(int ownerId, int bookingId, Boolean isApproved);

    BookingDtoOut getById(int userId, int bookingId);

    List<BookingDtoOut> getByBooker(int bookerId, BookingState state, PageRequest pageRequest);

    List<BookingDtoOut> getByOwner(int ownerId, BookingState state, PageRequest pageRequest);
}
