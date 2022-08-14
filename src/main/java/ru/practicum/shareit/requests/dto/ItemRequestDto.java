package ru.practicum.shareit.requests.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ItemRequestDto {
    private int id;
    private String description;
    private Integer requesterId;
    private LocalDate creationDate;
}
