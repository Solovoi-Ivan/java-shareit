package ru.practicum.shareit.item;

import lombok.Data;
import lombok.NonNull;

@Data
public class Item {
    private final int id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private Boolean available;
    @NonNull
    private Integer ownerId;
    private int requestId;
}
