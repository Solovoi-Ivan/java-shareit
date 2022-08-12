package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemRepository {
    List<ItemDto> getAll();
    ItemDto getById(int itemId);
    ItemDto create(ItemDto item);
    ItemDto update(int itemId, ItemDto item);
    void delete(int itemId);
}
