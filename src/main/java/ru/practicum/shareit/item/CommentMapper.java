package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class CommentMapper {
    public Comment toEntity(CommentDtoIn comment, Item item, User author) {
        return new Comment(comment.getText(), item, author, LocalDateTime.now());
    }

    public CommentDtoOut fromEntity(Comment c) {
        return new CommentDtoOut(c.getId(), c.getText(), c.getAuthor().getName(), c.getCreated());
    }
}
