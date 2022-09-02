package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;

@Component
public class ItemMapper {
    public Item fromDto(int itemId, int ownerId, ItemDto item) {
        return new Item(itemId, item.getName(), item.getDescription(),
                item.getAvailable(), ownerId);
    }

    public ItemDto toDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(),
                item.getAvailable(), item.getRequestId());
    }
}
