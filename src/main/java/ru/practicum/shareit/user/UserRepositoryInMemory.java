package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryInMemory implements UserRepository {
    private final UserMapper userMapper;
    private final Map<Integer, User> userMap = new HashMap<>();
    private int id;

    @Override
    public List<UserDto> getAll() {
        return userMap.values().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getById(int userId) {
        return userMapper.toDto(userMap.get(userId));
    }

    @Override
    public UserDto create(UserDto user) {
        id++;
        userMap.put(id, userMapper.fromDto(id, user));
        return userMapper.toDto(userMap.get(id));
    }

    @Override
    public UserDto update(UserDto user) {
        int userId = user.getId();
        if (user.getName() != null) {
            userMap.get(userId).setName(user.getName());
        }
        if (user.getEmail() != null) {
            userMap.get(userId).setEmail(user.getEmail());
        }
        return userMapper.toDto(userMap.get(userId));
    }

    @Override
    public void delete(int userId) {
        userMap.remove(userId);
    }
}