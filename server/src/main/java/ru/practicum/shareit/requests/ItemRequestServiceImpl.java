package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.requests.dto.ItemRequestDtoIn;
import ru.practicum.shareit.requests.dto.ItemRequestDtoOut;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapper mapper;
    private final ItemMapper itemMapper;

    @Override
    public ItemRequestDtoOut create(ItemRequestDtoIn itemRequest, int requesterId) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("Неверно указан пользователь"));
        return mapper.fromEntity(itemRequestRepository
                .save(mapper.toEntity(itemRequest, requester, LocalDateTime.now())), new ArrayList<>());
    }

    @Override
    public List<ItemRequestDtoOut> getByRequester(int requesterId) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("Неверно указан пользователь"));
        return itemRequestRepository.findByRequester(requester.getId())
                .stream()
                .sorted(Comparator.comparing(ItemRequest::getCreated).reversed())
                .map(i -> mapper.fromEntity(i, itemRepository.findByRequestId(i.getId())
                        .stream()
                        .map(itemMapper::fromEntity)
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDtoOut> getAll(int userId, PageRequest pageRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Неверно указан пользователь"));
        return itemRequestRepository.findAll()
                .stream()
                .filter(i -> !i.getRequester().equals(user))
                .map(i -> mapper.fromEntity(i, itemRepository.findByRequestId(i.getId())
                        .stream()
                        .map(itemMapper::fromEntity)
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDtoOut getById(int requestId, int userId) {
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Неверно указан пользователь"));
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Запрос не найден"));
        List<ItemDtoOut> list = itemRepository.findByRequestId(requestId)
                .stream()
                .map(itemMapper::fromEntity)
                .collect(Collectors.toList());
        return mapper.fromEntity(itemRequest, list);
    }
}