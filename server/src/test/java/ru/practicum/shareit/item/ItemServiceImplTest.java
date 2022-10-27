package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.util.BookingStatus;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class ItemServiceImplTest {
    ItemService itemService;
    ItemRepository itemRepository;
    UserRepository userRepository;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;
    ItemMapper mapper = new ItemMapper();
    CommentMapper commentMapper = new CommentMapper();
    Item item;
    User user1;
    User user2;
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("id"));
    LocalDateTime start = LocalDateTime.of(2020, 1, 1, 1, 1);
    LocalDateTime end = LocalDateTime.of(2021, 1, 1, 1, 1);

    @BeforeEach
    void fill() {
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        bookingRepository = mock(BookingRepository.class);
        commentRepository = mock(CommentRepository.class);
        itemService = new ItemServiceImpl(itemRepository, userRepository,
                bookingRepository, commentRepository, mapper, commentMapper);
        user1 = new User("user", "user@mail.ru");
        user1.setId(1);
        user2 = new User("user2", "user2@mail.ru");
        user2.setId(2);
        item = new Item("item", "desc", true, user1);
    }

    @Test
    void getByOwnerTest() {
        item.setId(1);
        when(itemRepository.findByOwnerId(1, pageRequest))
                .thenReturn(List.of(item));
        List<ItemDtoOutWithBooking> list = itemService.getByOwner(user1.getId(), pageRequest);

        assertNotNull(list);
        assertEquals(list.size(), 1);
        assertEquals(list.get(0), mapper.fromEntityAddBookings(item, null, null, List.of()));

        verify(itemRepository, times(1)).findByOwnerId(1, pageRequest);
    }

    @Test
    void getByIdTest() {
        item.setId(1);
        Booking lastBooking = new Booking(start, end, item, user2, BookingStatus.APPROVED);
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        when(bookingRepository.getLastBooking(1, LocalDateTime.now())).thenReturn(lastBooking);
        ItemDtoOutWithBooking testItem = itemService.getById(item.getId(), user1.getId());

        assertNotNull(testItem);
        assertEquals(testItem.getId(), item.getId());
        assertThrows(EntityNotFoundException.class, () -> itemService.getById(2, 1));

        verify(itemRepository, times(1)).findById(1);
    }

    @Test
    void searchTest() {
        item.setId(1);
        String text = "";
        when(itemRepository.search("item", pageRequest)).thenReturn(List.of(item));

        assertEquals(itemService.search(text, pageRequest), List.of());

        text = "item";
        List<ItemDtoOut> list = itemService.search(text, pageRequest);

        assertNotNull(list);
        assertEquals(list.size(), 1);
        assertEquals(list.get(0), mapper.fromEntity(item));
    }

    @Test
    void createTest() {
        ItemDtoIn itemDtoIn = new ItemDtoIn();
        itemDtoIn.setName("item");
        itemDtoIn.setDescription("desc");
        itemDtoIn.setAvailable(true);
        when(itemRepository.save(item)).thenReturn(item);
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));

        assertThrows(RuntimeException.class, () -> itemService.create(2, itemDtoIn));

        ItemDtoOut testItem = itemService.create(user1.getId(), itemDtoIn);

        assertNotNull(testItem);
        assertEquals(testItem, mapper.fromEntity(item));

        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void updateTest() {
        ItemDtoIn itemDtoIn = new ItemDtoIn();
        itemDtoIn.setName("item");
        itemDtoIn.setDescription("desc");
        itemDtoIn.setAvailable(true);
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);

        assertThrows(NullPointerException.class, () -> itemService.update(1, itemDtoIn));

        itemDtoIn.setId(1);

        assertThrows(RuntimeException.class, () -> itemService.update(2, itemDtoIn));

        ItemDtoOut testItem = itemService.update(1, itemDtoIn);

        assertNotNull(testItem);
        assertEquals(testItem, mapper.fromEntity(item));

        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void deleteTest() {
        itemService.delete(1);

        verify(itemRepository, times(1)).deleteById(1);
    }

    @Test
    void addCommentTest() {
        item.setId(1);
        Booking booking = new Booking(LocalDateTime.of(2000, 1, 1, 1, 1),
                LocalDateTime.of(2000, 1, 1, 1, 1),
                item, user1, BookingStatus.APPROVED);
        CommentDtoIn commentDtoIn = new CommentDtoIn("text");
        Comment comment = commentMapper.toEntity(commentDtoIn, item, user1);
        comment.setId(1);
        when(commentRepository.save(any()))
                .thenReturn(comment);
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        assertThrows(ValidationException.class, () -> itemService.addComment(commentDtoIn, 1, 1));

        when(bookingRepository.findAll()).thenReturn(List.of(booking));

        assertThrows(EntityNotFoundException.class, () -> itemService.addComment(commentDtoIn, 2, 1));
        assertThrows(EntityNotFoundException.class, () -> itemService.addComment(commentDtoIn, 1, 2));

        CommentDtoOut testComment = itemService.addComment(commentDtoIn, item.getId(), user1.getId());

        assertNotNull(testComment);
        assertEquals(testComment, commentMapper.fromEntity(comment));

        verify(commentRepository, times(1)).save(any());
    }
}