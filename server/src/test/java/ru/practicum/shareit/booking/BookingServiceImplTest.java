package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.util.BookingState;
import ru.practicum.shareit.util.BookingStatus;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class BookingServiceImplTest {
    BookingService bookingService;
    ItemRepository itemRepository;
    UserRepository userRepository;
    BookingRepository bookingRepository;
    BookingMapper mapper = new BookingMapper(new UserMapper(), new ItemMapper());
    Item item;
    User owner;
    User booker;
    Booking booking;
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("start").descending());
    LocalDateTime start = LocalDateTime.of(2023, 1, 1, 1, 1);
    LocalDateTime end = LocalDateTime.of(2024, 1, 1, 1, 1);

    @BeforeEach
    void fill() {
        bookingRepository = mock(BookingRepository.class);
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);

        bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository, mapper);
        owner = new User("user1", "user1@mail.ru");
        owner.setId(1);
        booker = new User("user2", "user2@mail.ru");
        booker.setId(2);
        item = new Item("item", "desc", true, owner);
        item.setId(1);
        booking = new Booking(start, end, item, booker, BookingStatus.APPROVED);
        booking.setId(1);
    }

    @Test
    void addBooking() {
        BookingDtoIn bookingDtoIn = new BookingDtoIn(1, start, end);
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        when(userRepository.findById(1)).thenReturn(Optional.of(booker));
        when(bookingRepository.save(any())).thenReturn(booking);

        assertThrows(EntityNotFoundException.class, () -> bookingService.addBooking(bookingDtoIn, 3));
        assertThrows(EntityNotFoundException.class, () -> bookingService.addBooking(bookingDtoIn, 2));

        bookingDtoIn.setItemId(2);

        assertThrows(EntityNotFoundException.class, () -> bookingService.addBooking(bookingDtoIn, 1));

        bookingDtoIn.setItemId(1);
        BookingDtoOut testBooking = bookingService.addBooking(bookingDtoIn, 1);

        assertNotNull(testBooking);
        assertEquals(testBooking, mapper.fromEntity(booking));

        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    void approveBooking() {
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);

        assertThrows(EntityNotFoundException.class,
                () -> bookingService.approveBooking(1, 2, true));
        assertThrows(EntityNotFoundException.class,
                () -> bookingService.approveBooking(2, 1, true));

        booking.setStatus(BookingStatus.APPROVED);

        assertThrows(ValidationException.class,
                () -> bookingService.approveBooking(1, 1, true));

        booking.setStatus(BookingStatus.WAITING);
        BookingDtoOut testBooking = bookingService.approveBooking(1, 1, true);

        assertNotNull(testBooking);
        assertEquals(testBooking, mapper.fromEntity(booking));

        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void getBookingById() {
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));

        assertThrows(EntityNotFoundException.class,
                () -> bookingService.getBookingById(1, 2));

        assertThrows(EntityNotFoundException.class,
                () -> bookingService.getBookingById(3, 1));

        BookingDtoOut testBooking = bookingService.getBookingById(1, 1);

        assertNotNull(testBooking);
        assertEquals(testBooking, mapper.fromEntity(booking));

        verify(bookingRepository, times(2)).findById(1);
    }

    @Test
    void getBookingByBooker() {
        when(userRepository.findById(1)).thenReturn(Optional.of(booker));
        when(bookingRepository.findByBooker(booker, pageRequest))
                .thenReturn(List.of(booking));

        assertThrows(EntityNotFoundException.class,
                () -> bookingService.getBookingByBooker(2, BookingState.ALL, pageRequest));

        List<BookingDtoOut> list = bookingService.getBookingByBooker(1, BookingState.ALL, pageRequest);

        assertNotNull(list);
        assertEquals(list.size(), 1);
        assertEquals(list.get(0), mapper.fromEntity(booking));

        verify(bookingRepository, times(1)).findByBooker(booker, pageRequest);
    }

    @Test
    void getBookingByOwner() {
        when(userRepository.findById(2)).thenReturn(Optional.of(owner));
        when(bookingRepository.findByOwner(owner, pageRequest))
                .thenReturn(List.of(booking));

        assertThrows(EntityNotFoundException.class,
                () -> bookingService.getBookingByOwner(1, BookingState.ALL, pageRequest));

        List<BookingDtoOut> list = bookingService.getBookingByOwner(2, BookingState.ALL, pageRequest);

        assertNotNull(list);
        assertEquals(list.size(), 1);
        assertEquals(list.get(0), mapper.fromEntity(booking));

        list = bookingService.getBookingByOwner(2, BookingState.CURRENT, pageRequest);

        assertNotNull(list);
        assertEquals(list.size(), 0);

        list = bookingService.getBookingByOwner(2, BookingState.PAST, pageRequest);

        assertNotNull(list);
        assertEquals(list.size(), 0);

        list = bookingService.getBookingByOwner(2, BookingState.FUTURE, pageRequest);

        assertNotNull(list);
        assertEquals(list.size(), 1);
        assertEquals(list.get(0), mapper.fromEntity(booking));

        list = bookingService.getBookingByOwner(2, BookingState.WAITING, pageRequest);

        assertNotNull(list);
        assertEquals(list.size(), 0);

        list = bookingService.getBookingByOwner(2, BookingState.REJECTED, pageRequest);

        assertNotNull(list);
        assertEquals(list.size(), 0);

        verify(bookingRepository, times(6)).findByOwner(owner, pageRequest);
    }
}