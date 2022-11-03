package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    User user1;
    Item item1;
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("id"));

    @BeforeEach
    void fill() {
        user1 = userRepository.save(new User("user1", "user1@mail.ru"));
        item1 = itemRepository.save(new Item("item1", "item1 desc", true, user1));
    }

    @Test
    void findByOwnerId() {
        List<Item> list = itemRepository.findByOwnerId(1, pageRequest);

        assertNotNull(list);
        assertEquals(list.size(), 1);
        assertEquals(list.get(0), item1);
    }

    @Test
    void search() {
        List<Item> testItems = itemRepository.search("item1", pageRequest);

        assertNotNull(testItems);
        assertEquals(testItems.size(), 1);
        assertEquals(testItems.get(0), item1);
    }

    @AfterEach
    void clear() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }
}