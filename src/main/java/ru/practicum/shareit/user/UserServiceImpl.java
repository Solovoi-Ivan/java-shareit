package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.DuplicateEntryException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAll() {
        return userRepository.getAll();
    }

    @Override
    public UserDto getById(int userId) {
        return userRepository.getById(userId);
    }

    @Override
    public UserDto create(UserDto user) {
        fieldsValidation(user);
        return userRepository.create(duplicateEmailValidation(user));
    }

    @Override
    public UserDto update(int userId, UserDto user) {
        return userRepository.update(userId, duplicateEmailValidation(user));
    }

    @Override
    public void delete(int userId) {
        userRepository.delete(userId);
    }

    public UserDto duplicateEmailValidation(UserDto user) {
        for (UserDto userDto : userRepository.getAll()) {
            if (user.getEmail() != null && userDto.getEmail().equals(user.getEmail())) {
                throw new DuplicateEntryException("Пользователь с таким email уже существует");
            }
        }
        return user;
    }

    public void fieldsValidation(UserDto user) {
        if (user.getName() == null || user.getEmail() == null) {
            throw new ValidationException("У пользователя не указано имя или email");
        }
    }
}
