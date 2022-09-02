package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.ArrayList;
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

    @Override
    public List<ItemDto> getByOwner(int ownerId) {
        return itemMap.values().stream()
                .filter(i -> i.getOwnerId() == ownerId)
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getById(int itemId) {
        return itemMapper.toDto(itemMap.get(itemId));
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        List<Item> list = new ArrayList<>();
        list.addAll(itemMap.values().stream()
                .filter(i -> i.getName().toLowerCase().contains(text.toLowerCase()))
                .filter(i -> i.getAvailable().equals(true))
                .collect(Collectors.toList()));
        list.addAll(itemMap.values().stream()
                .filter(i -> i.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(i -> i.getAvailable().equals(true))
                .filter(i -> !list.contains(i))
                .collect(Collectors.toList()));
        return list.stream().map(itemMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto create(int ownerId, ItemDto item) {
        id++;
        itemMap.put(id, itemMapper.fromDto(id, ownerId, item));
        return itemMapper.toDto(itemMap.get(id));
    }

    @Override
    public ItemDto update(int ownerId, ItemDto item) {
        int itemId = item.getId();
        if (itemMap.get(itemId).getOwnerId() != ownerId) {
            throw new NotFoundException("Владелец вещи с указанным id не найден");
        }
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

    @Override
    public void delete(int itemId) {
        itemMap.remove(itemId);
    }
}