package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    public List<UserDto> getAll(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest)
                .stream()
                .map(mapper::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getById(int userId) {
        return mapper.fromEntity(userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")));
    }

    @Override
    public UserDto create(UserDto user) {
        return mapper.fromEntity(userRepository.save(mapper.toEntity(user)));
    }

    @Override
    public UserDto update(UserDto user) {
        int userId = user.getId();
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        if (user.getName() != null) {
            if (user.getName().isBlank()) {
                throw new ValidationException("У пользователя пустое имя");
            }
            u.setName(user.getName());
        }
        if (user.getEmail() != null) {
            u.setEmail(user.getEmail());
        }
        return mapper.fromEntity(userRepository.save(u));
    }

    @Override
    public void delete(int userId) {
        userRepository.deleteById(userId);
    }
}