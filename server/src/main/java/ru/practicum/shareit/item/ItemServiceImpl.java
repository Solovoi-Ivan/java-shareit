package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper mapper;
    private final CommentMapper commentMapper;

    @Override
    public List<ItemDtoOutWithBooking> getByOwner(int ownerId, PageRequest pageRequest) {
        return itemRepository.findByOwnerId(ownerId, pageRequest)
                .stream()
                .map(this::addBookingInfo)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDtoOutWithBooking getById(int itemId, int ownerId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Вещь не найдена"));
        ItemDtoOutWithBooking itemDto = addBookingInfo(item);
        if (!item.getOwner().getId().equals(ownerId)) {
            itemDto.setLastBooking(null);
            itemDto.setNextBooking(null);
        }
        return itemDto;
    }

    @Override
    public List<ItemDtoOut> search(String text, PageRequest pageRequest) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.search(text, pageRequest).stream()
                .map(mapper::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDtoOut create(int ownerId, ItemDtoIn item) {
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Неверно указан пользователь"));
        return mapper.fromEntity(itemRepository.save(mapper.toEntity(item, user)));
    }

    @Override
    public ItemDtoOut update(int ownerId, ItemDtoIn item) {
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Неверно указан пользователь"));
        int itemId = item.getId();
        Item i = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Вещь не найдена"));
        if (!i.getOwner().equals(user)) {
            throw new EntityNotFoundException("Вещь, принадлежащая данному пользователю, не найдена");
        }
        if (item.getName() != null) {
            i.setName(item.getName());
        }
        if (item.getDescription() != null) {
            i.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            i.setAvailable(item.getAvailable());
        }
        return mapper.fromEntity(itemRepository.save(i));
    }

    @Override
    public void delete(int itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public CommentDtoOut addComment(CommentDtoIn comment, int itemId, int userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Вещь не найдена"));
        List<Booking> list = bookingRepository.findAll().stream()
                .filter(b -> b.getItem().equals(item))
                .filter(b -> b.getBooker().equals(user))
                .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
        if (list.isEmpty()) {
            throw new ValidationException("Вы не пользовались этой вещью");
        }
        return commentMapper.fromEntity(commentRepository.save(commentMapper.toEntity(comment, item, user)));
    }

    public ItemDtoOutWithBooking addBookingInfo(Item item) {
        LocalDateTime now = LocalDateTime.now();
        Booking lastBooking = bookingRepository.getLastBooking(item.getId(), now);
        Booking nextBooking = bookingRepository.getNextBooking(item.getId(), now);
        List<CommentDtoOut> comment = commentRepository.findAll().stream()
                .filter(c -> c.getItem().equals(item))
                .map(commentMapper::fromEntity)
                .collect(Collectors.toList());
        return mapper.fromEntityAddBookings(item, lastBooking, nextBooking, comment);
    }
}