package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemRepository {
    List<ItemDto> getByOwner(int ownerId);

    ItemDto getById(int itemId);

    List<ItemDto> search(String query);

    ItemDto create(int ownerId, ItemDto item);

    ItemDto update(int ownerId, ItemDto item);

    void delete(int itemId);
}
