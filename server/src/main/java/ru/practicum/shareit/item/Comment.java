package ru.practicum.shareit.item;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "COMMENTS")
@NoArgsConstructor
@RequiredArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int id;
    @NonNull
    @Column(name = "comment_text", nullable = false)
    private String text;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    @NonNull
    @Column(name = "created", nullable = false)
    LocalDateTime created;
}
