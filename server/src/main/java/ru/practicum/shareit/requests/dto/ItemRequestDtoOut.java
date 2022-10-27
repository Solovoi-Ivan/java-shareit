package ru.practicum.shareit.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDtoOut;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemRequestDtoOut {
    private int id;
    private String description;
    private LocalDateTime created;
    private List<ItemDtoOut> items;
}