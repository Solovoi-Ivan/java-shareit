package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.DuplicateEntryException;
import ru.practicum.shareit.exceptions.NotFoundException;
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
        for (UserDto user : getAll()) {
            if (userId == user.getId()) {
                return userRepository.getById(userId);
            }
        }
        throw new NotFoundException("Пользователь с указанным id не найден");
    }

    @Override
    public UserDto create(UserDto user) {
        fieldsValidation(user);
        return userRepository.create(duplicateEmailValidation(user));
    }

    @Override
    public UserDto update(UserDto user) {
        return userRepository.update(duplicateEmailValidation(user));
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
