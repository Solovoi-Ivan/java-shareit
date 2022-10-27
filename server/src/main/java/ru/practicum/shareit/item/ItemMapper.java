package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.dto.ItemDtoOutWithBooking;
import ru.practicum.shareit.user.User;

import java.util.List;

@Component
@AllArgsConstructor
public class ItemMapper {
    public Item toEntity(ItemDtoIn item, User owner) {
        Item i = new Item(item.getName(), item.getDescription(),
                item.getAvailable(), owner);
        if (item.getRequestId() != null) {
            i.setRequestId(item.getRequestId());
        }
        return i;
    }

    public ItemDtoOut fromEntity(Item item) {
        return new ItemDtoOut(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                item.getRequestId());
    }

    public ItemDtoOutWithBooking fromEntityAddBookings(Item item, Booking lastBooking,
                                                       Booking nextBooking, List<CommentDtoOut> comment) {
        BookingDtoForItem lastBookingDto;
        BookingDtoForItem nextBookingDto;
        if (lastBooking != null) {
            lastBookingDto = new BookingDtoForItem(lastBooking.getId(), lastBooking.getBooker().getId());
        } else {
            lastBookingDto = null;
        }
        if (nextBooking != null) {
            nextBookingDto = new BookingDtoForItem(nextBooking.getId(), nextBooking.getBooker().getId());
        } else {
            nextBookingDto = null;
        }
        return new ItemDtoOutWithBooking(item.getId(), item.getName(), item.getDescription(),
                item.getAvailable(), item.getRequestId(), lastBookingDto, nextBookingDto, comment);
    }
}
