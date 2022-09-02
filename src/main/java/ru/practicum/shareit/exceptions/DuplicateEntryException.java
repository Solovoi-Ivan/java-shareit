package ru.practicum.shareit.exceptions;

public class DuplicateEntryException extends RuntimeException {
    public DuplicateEntryException(final String message) {
        super(message);
    }
}
