package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.util.BookingStatus;
import ru.practicum.shareit.user.UserMapper;

@Component
@AllArgsConstructor
public class BookingMapper {
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    public Booking toEntity(BookingDtoIn b, Item item, User booker, BookingStatus status) {
        return new Booking(b.getStart(), b.getEnd(), item, booker, status);
    }

    public BookingDtoOut fromEntity(Booking b) {
        return new BookingDtoOut(b.getId(), b.getStart(), b.getEnd(),
                itemMapper.fromEntity(b.getItem()), userMapper.fromEntity(b.getBooker()), b.getStatus());
    }
}