package ru.practicum.shareit.requests;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class ItemRequest {
    private final int id;
    @NonNull
    private String description;
    @NonNull
    private Integer requesterId;
    @NonNull
    private LocalDate creationDate;
}
