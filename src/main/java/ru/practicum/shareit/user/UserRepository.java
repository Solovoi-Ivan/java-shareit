package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserRepository {
    List<UserDto> getAll();
    UserDto getById(int userId);
    UserDto create(UserDto user);
    UserDto update(int userId, UserDto user);
    void delete(int userId);
}
