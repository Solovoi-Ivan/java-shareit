package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.util.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    BookingRepository bookingRepository;

    User user1;
    User user2;
    Item item1;
    Booking booking1;
    LocalDateTime start = LocalDateTime.of(2023, 1, 1, 1, 1);
    LocalDateTime end = LocalDateTime.of(2023, 1, 1, 1, 1);
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("start"));

    @BeforeEach
    void fill() {
        user1 = userRepository.save(new User("user1", "user1@mail.ru"));
        user2 = userRepository.save(new User("user2", "user2@mail.ru"));
        item1 = itemRepository.save(new Item("item1", "item1 desc", true, user1));
        booking1 = bookingRepository.save(new Booking(start, end, item1, user2, BookingStatus.WAITING));
    }

    @Test
    void findByOwner() {
        List<Booking> list = bookingRepository.findByOwner(user1, pageRequest);

        assertNotNull(list);
        assertEquals(list.size(), 1);
        assertEquals(list.get(0), booking1);
    }

    @Test
    void findByBooker() {
        List<Booking> list = bookingRepository.findByBooker(user2, pageRequest);

        assertNotNull(list);
        assertEquals(list.size(), 1);
        assertEquals(list.get(0), booking1);
    }

    @AfterEach
    void clear() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        bookingRepository.deleteAll();
    }
}