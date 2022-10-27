package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemDtoOutWithBooking {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private Integer requestId;
    private BookingDtoForItem lastBooking;
    private BookingDtoForItem nextBooking;
    private List<CommentDtoOut> comments;
}
