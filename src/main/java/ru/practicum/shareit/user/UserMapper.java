package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;

@Component
public class UserMapper {
    public User toEntity(UserDto user) {
        return new User(user.getName(), user.getEmail());
    }

    public UserDto fromEntity(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}