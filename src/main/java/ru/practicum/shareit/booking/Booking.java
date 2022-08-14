package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.util.BookingStatus;

import java.time.LocalDate;

@Data
public class Booking {
    private final int id;
    @NonNull
    private LocalDate start;
    @NonNull
    private LocalDate end;
    @NonNull
    private Integer itemId;
    @NonNull
    private Integer bookerId;
    @NonNull
    private BookingStatus status;
}
