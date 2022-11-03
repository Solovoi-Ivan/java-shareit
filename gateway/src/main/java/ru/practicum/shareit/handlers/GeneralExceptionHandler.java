package ru.practicum.shareit.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.practicum.shareit.exceptions.UnsupportedStateException;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.Map;


@RestControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleFailedValidation(final ValidationException e) {
        return Map.of("error", "NOT VALID", "message", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleUnsupportedState(final UnsupportedStateException e) {
        return Map.of("error", e.getMessage());
    }
}