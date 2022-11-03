package ru.practicum.shareit.requests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemRequestRepository itemRequestRepository;

    User user1;
    User user2;
    Item item1;
    ItemRequest request1;

    @BeforeEach
    void fill() {
        user1 = userRepository.save(new User("user1", "user1@mail.ru"));
        user2 = userRepository.save(new User("user2", "user2@mail.ru"));
        item1 = itemRepository.save(new Item("item1", "item1 desc", true, user1));
        request1 = itemRequestRepository.save(new ItemRequest("item1", user2, LocalDateTime.now()));
    }

    @Test
    void findByRequester() {
        List<ItemRequest> list = itemRequestRepository.findByRequester(2);

        assertNotNull(list);
        assertEquals(list.size(), 1);
        assertEquals(list.get(0), request1);
    }

    @AfterEach
    void clear() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }
}