package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {
    List<ItemDtoOutWithBooking> getByOwner(int userId);

    ItemDtoOutWithBooking getById(int itemId, int userId);

    List<ItemDtoOut> search(String query);

    ItemDtoOut create(int ownerId/*UserDto owner*/, ItemDtoIn item);

    ItemDtoOut update(int ownerId, ItemDtoIn item);

    void delete(int itemId);

    CommentDtoOut addComment(CommentDtoIn comment, int itemId, int userId);
}