package ru.practicum.shareit.user;

import lombok.Data;
import lombok.NonNull;

@Data
public class User {
    private final int id;
    @NonNull
    private String name;
    @NonNull
    private String email;
}
