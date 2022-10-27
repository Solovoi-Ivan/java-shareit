package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class UserServiceImplTest {

    UserService userService;
    UserRepository userRepository;
    UserMapper mapper = new UserMapper();
    User user;
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("id"));

    @BeforeEach
    void fill() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository, mapper);
        user = new User("user", "user@mail.ru");
    }

    @Test
    void getAllTest() {
        user.setId(1);
        when(userRepository.findAll(pageRequest))
                .thenReturn(new PageImpl<>(List.of(user)));
        List<UserDto> list = userService.getAll(pageRequest);

        assertNotNull(list);
        assertEquals(list.size(), 1);
        assertEquals(list.get(0), mapper.fromEntity(user));

        verify(userRepository, times(1)).findAll(pageRequest);
    }

    @Test
    void getByIdTest() {
        user.setId(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        UserDto testUser = userService.getById(user.getId());

        assertNotNull(testUser);
        assertEquals(testUser, mapper.fromEntity(user));
        assertThrows(EntityNotFoundException.class, () -> userService.getById(2));

        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void createTest() {
        when(userRepository.save(user)).thenReturn(user);
        UserDto testUser = userService.create(mapper.fromEntity(user));

        assertNotNull(testUser);
        assertEquals(testUser, mapper.fromEntity(user));

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateTest() {
        when(userRepository.save(user)).thenReturn(user);

        assertThrows(NullPointerException.class, () -> userService.update(mapper.fromEntity(user)));

        user.setId(1);

        assertThrows(EntityNotFoundException.class, () -> userService.update(mapper.fromEntity(user)));

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        UserDto testUser = userService.update(mapper.fromEntity(user));

        assertNotNull(testUser);
        assertEquals(testUser, mapper.fromEntity(user));

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void deleteTest() {
        userService.delete(1);

        verify(userRepository, times(1)).deleteById(1);
    }
}