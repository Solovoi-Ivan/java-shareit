package ru.practicum.shareit.requests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.requests.dto.ItemRequestDtoIn;
import ru.practicum.shareit.requests.dto.ItemRequestDtoOut;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class ItemRequestServiceImplTest {
    ItemRequestService itemRequestService;
    ItemRepository itemRepository;
    UserRepository userRepository;
    ItemRequestRepository itemRequestRepository;
    ItemRequestMapper mapper = new ItemRequestMapper();
    ItemMapper itemMapper = new ItemMapper();
    Item item;
    User requester;
    User user;
    ItemRequest itemRequest;
    LocalDateTime time = LocalDateTime.now();
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("creationDate"));

    @BeforeEach
    void fill() {
        itemRequestRepository = mock(ItemRequestRepository.class);
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);

        itemRequestService = new ItemRequestServiceImpl(itemRepository, userRepository,
                itemRequestRepository, mapper, itemMapper);
        requester = new User("user1", "user1@mail.ru");
        requester.setId(1);
        user = new User("user2", "user2@mail.ru");
        user.setId(2);
        item = new Item("item", "desc", true, requester);
        item.setId(1);
        itemRequest = new ItemRequest("desc", requester, time);
        itemRequest.setId(1);
    }

    @Test
    void create() {
        ItemRequestDtoIn itemRequestDtoIn = new ItemRequestDtoIn("desc");
        when(userRepository.findById(1)).thenReturn(Optional.of(requester));
        when(itemRequestRepository.save(any())).thenReturn(itemRequest);

        assertThrows(RuntimeException.class,
                () -> itemRequestService.create(itemRequestDtoIn, 2));

        ItemRequestDtoOut testRequest = itemRequestService.create(itemRequestDtoIn, 1);

        assertNotNull(testRequest);
        assertEquals(testRequest, mapper.fromEntity(itemRequest, List.of()));

        verify(itemRequestRepository, times(1)).save(any());
    }

    @Test
    void getByRequester() {
        when(userRepository.findById(1)).thenReturn(Optional.of(requester));
        when(itemRepository.findByRequestId(1)).thenReturn(List.of());
        when(itemRequestRepository.findByRequester(1)).thenReturn(List.of(itemRequest));


        assertThrows(RuntimeException.class,
                () -> itemRequestService.getByRequester(2));

        List<ItemRequestDtoOut> list = itemRequestService.getByRequester(1);

        assertNotNull(list);
        assertEquals(list.size(), 1);
        assertEquals(list.get(0), mapper.fromEntity(itemRequest, List.of()));

        verify(itemRequestRepository, times(1)).findByRequester(1);
    }

    @Test
    void getAll() {
        when(userRepository.findById(1)).thenReturn(Optional.of(requester));
        when(userRepository.findById(2)).thenReturn(Optional.of(user));
        when(itemRepository.findByRequestId(1)).thenReturn(List.of());
        when(itemRequestRepository.findAll()).thenReturn(List.of(itemRequest));

        assertThrows(RuntimeException.class,
                () -> itemRequestService.getAll(3, pageRequest));

        List<ItemRequestDtoOut> list = itemRequestService.getAll(2, pageRequest);

        assertNotNull(list);
        assertEquals(list.size(), 1);
        assertEquals(list.get(0), mapper.fromEntity(itemRequest, List.of()));

        verify(itemRequestRepository, times(1)).findAll();
    }

    @Test
    void getById() {
        when(userRepository.findById(1)).thenReturn(Optional.of(requester));
        when(itemRepository.findByRequestId(1)).thenReturn(List.of());
        when(itemRequestRepository.findById(1)).thenReturn(Optional.of(itemRequest));

        assertThrows(RuntimeException.class,
                () -> itemRequestService.getById(1, 2));
        assertThrows(EntityNotFoundException.class,
                () -> itemRequestService.getById(2, 1));

        ItemRequestDtoOut testRequest = itemRequestService.getById(1, 1);

        assertNotNull(testRequest);
        assertEquals(testRequest, mapper.fromEntity(itemRequest, List.of()));

        verify(itemRequestRepository, times(1)).findById(1);
    }
}