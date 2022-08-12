package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

public class ItemMapper {
    public Item fromDto(int itemId, ItemDto item) {
        return new Item(itemId, item.getName(), item.getDescription(), item.getAvailable(), item.getOwnerId());
    }

    public ItemDto toDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), item.getOwnerId(), item.getRequestId());
    }
}
