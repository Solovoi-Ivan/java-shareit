package ru.practicum.shareit.user;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAll(PageRequest pageRequest);

    UserDto getById(int userId);

    UserDto create(UserDto user);

    UserDto update(UserDto user);

    void delete(int userId);
}