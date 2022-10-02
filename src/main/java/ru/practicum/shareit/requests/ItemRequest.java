package ru.practicum.shareit.requests;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "REQUESTS")
@NoArgsConstructor
@RequiredArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private int id;
    @NonNull
    @Column(name = "description", nullable = false)
    private String description;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;
    @NonNull
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
}
