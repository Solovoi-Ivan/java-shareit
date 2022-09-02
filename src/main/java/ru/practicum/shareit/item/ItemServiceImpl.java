package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public List<ItemDto> getByOwner(int ownerId) {
        return itemRepository.getByOwner(ownerId);
    }

    @Override
    public ItemDto getById(int itemId) {
        return itemRepository.getById(itemId);
    }

    @Override
    public List<ItemDto> search(String text) {
        return itemRepository.search(text);
    }

    @Override
    public ItemDto create(int ownerId, ItemDto item) {
        fieldsValidation(item);
        return itemRepository.create(ownerId, item);
    }

    @Override
    public ItemDto update(int ownerId, ItemDto item) {
        return itemRepository.update(ownerId, item);
    }

    @Override
    public void delete(int itemId) {
        itemRepository.delete(itemId);
    }

    public void fieldsValidation(ItemDto item) {
        if (item.getName() == null || item.getDescription() == null || item.getAvailable() == null ||
                item.getName().isBlank() || item.getDescription().isBlank()) {
            throw new ValidationException("У предмета не указано название, описание или статус");
        }
    }
}
