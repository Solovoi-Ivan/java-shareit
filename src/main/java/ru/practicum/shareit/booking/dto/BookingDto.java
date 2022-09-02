package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.util.BookingStatus;

import java.time.LocalDate;

@Data
public class BookingDto {
    private int id;
    private LocalDate start;
    private LocalDate end;
    private Integer itemId;
    private Integer bookerId;
    private BookingStatus status;
}
