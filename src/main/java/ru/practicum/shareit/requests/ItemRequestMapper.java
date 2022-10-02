package ru.practicum.shareit.requests;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.requests.dto.ItemRequestDtoIn;
import ru.practicum.shareit.requests.dto.ItemRequestDtoOut;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class ItemRequestMapper {
    public ItemRequest toEntity(ItemRequestDtoIn itemRequest, User requester, LocalDateTime now) {
        return new ItemRequest(itemRequest.getDescription(), requester, now);
    }

    public ItemRequestDtoOut fromEntity(ItemRequest itemRequest, List<ItemDtoOut> items) {
        return new ItemRequestDtoOut(itemRequest.getId(), itemRequest.getDescription(),
                itemRequest.getCreated(), items);
    }
}
