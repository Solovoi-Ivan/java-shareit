package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryInMemory implements ItemRepository {
    private final ItemMapper itemMapper;
    private final Map<Integer, Item> itemMap = new HashMap<>();
    private int id;

    public List<ItemDto> getAll() {
        return itemMap.values().stream().map(itemMapper::toDto).collect(Collectors.toList());
    }

    public ItemDto getById(int itemId) {
        return itemMapper.toDto(itemMap.get(itemId));
    }

    public ItemDto create(ItemDto item) {
        id++;
        itemMap.put(id, itemMapper.fromDto(id, item));
        return itemMapper.toDto(itemMap.get(id));
    }

    public ItemDto update(int itemId, ItemDto item) {
        if (item.getName() != null) {
            itemMap.get(itemId).setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemMap.get(itemId).setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemMap.get(itemId).setAvailable(item.getAvailable());
        }
        return itemMapper.toDto(itemMap.get(itemId));
    }

    public void delete(int itemId) {
        itemMap.remove(itemId);
    }
}