package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.util.BookingState;
import ru.practicum.shareit.util.BookingStatus;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper mapper;

    @Override
    public BookingDtoOut addBooking(BookingDtoIn b, int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        Item item = itemRepository.findById(b.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("Вещь не найдена"));
        if (item.getOwner().equals(user)) {
            throw new EntityNotFoundException("Пользователь не найден");
        }
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь недоступна для бронирования");
        }
        bookingValidation(b.getStart(), b.getEnd());
        return mapper.fromEntity(bookingRepository
                .save(mapper.toEntity(b, item, user, BookingStatus.WAITING)));
    }

    @Override
    public BookingDtoOut approveBooking(int ownerId, int bookingId, Boolean isApproved) {
        Booking b = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Бронь не найдена"));
        if (!b.getItem().getOwner().getId().equals(ownerId)) {
            throw new EntityNotFoundException("Не найден владелец вещи");
        }
        if (isApproved && b.getStatus().equals(BookingStatus.WAITING)) {
            b.setStatus(BookingStatus.APPROVED);
        } else if (!isApproved && b.getStatus().equals(BookingStatus.WAITING)) {
            b.setStatus(BookingStatus.REJECTED);
        } else {
            throw new ValidationException("Бронь не ожидает подтверждения");
        }
        return mapper.fromEntity(bookingRepository.save(b));
    }

    @Override
    public BookingDtoOut getBookingById(int userId, int bookingId) {
        Booking b = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Бронь не найдена"));
        if (b.getBooker().getId().equals(userId) || b.getItem().getOwner().getId().equals(userId)) {
            return mapper.fromEntity(b);
        } else {
            throw new EntityNotFoundException("Некорректный id пользователя");
        }
    }

    @Override
    public List<BookingDtoOut> getBookingByBooker(int bookerId, BookingState state, PageRequest pageRequest) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        return bookingByState(bookingRepository.findByBooker(booker, pageRequest), state);
    }

    @Override
    public List<BookingDtoOut> getBookingByOwner(int ownerId, BookingState state, PageRequest pageRequest) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        return bookingByState(bookingRepository.findByOwner(owner, pageRequest), state);
    }

    public void bookingValidation(LocalDateTime start, LocalDateTime end) {
        if (start.isBefore(LocalDateTime.now()) || end.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Время бронирования не может быть в прошлом");
        }
        if (end.isBefore(start)) {
            throw new ValidationException("Завершение бронирования не может быть позже начала");
        }
    }

    public List<BookingDtoOut> bookingByState(List<Booking> list, BookingState state) {
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case ALL:
                return list.stream()
                        .map(mapper::fromEntity)
                        .collect(Collectors.toList());
            case CURRENT:
                return list.stream()
                        .filter(b -> b.getStart().isBefore(now))
                        .filter(i -> i.getEnd().isAfter(now))
                        .map(mapper::fromEntity)
                        .collect(Collectors.toList());
            case PAST:
                return list.stream()
                        .filter(b -> b.getEnd().isBefore(now))
                        .map(mapper::fromEntity)
                        .collect(Collectors.toList());
            case FUTURE:
                return list.stream()
                        .filter(b -> b.getStart().isAfter(now))
                        .map(mapper::fromEntity)
                        .collect(Collectors.toList());
            case WAITING:
                return list.stream()
                        .filter(b -> b.getStatus().equals(BookingStatus.WAITING))
                        .map(mapper::fromEntity)
                        .collect(Collectors.toList());
            case REJECTED:
                return list.stream()
                        .filter(b -> b.getStatus().equals(BookingStatus.REJECTED))
                        .map(mapper::fromEntity)
                        .collect(Collectors.toList());
            default:
                throw new ValidationException("Неверный параметр state");
        }
    }
}
