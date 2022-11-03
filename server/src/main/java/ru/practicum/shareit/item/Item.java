package ru.practicum.shareit.item;

import lombok.*;
import ru.practicum.shareit.user.User;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ITEMS")
@NoArgsConstructor
@RequiredArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Integer id;
    @NonNull
    @Column(name = "item_name", nullable = false)
    private String name;
    @NonNull
    @Column(name = "description", nullable = false)
    private String description;
    @NonNull
    @Column(name = "is_available", nullable = false)
    private Boolean available;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    @Column(name = "request_id")
    private int requestId;
}
