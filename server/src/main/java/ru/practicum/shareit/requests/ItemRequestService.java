package ru.practicum.shareit.requests;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.requests.dto.ItemRequestDtoIn;
import ru.practicum.shareit.requests.dto.ItemRequestDtoOut;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDtoOut create(ItemRequestDtoIn item, int requesterId);

    List<ItemRequestDtoOut> getByRequester(int requesterId);

    List<ItemRequestDtoOut> getAll(int userId, PageRequest pageRequest);

    ItemRequestDtoOut getById(int requestId, int userId);
}
