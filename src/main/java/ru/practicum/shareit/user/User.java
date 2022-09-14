package ru.practicum.shareit.user;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "USERS")
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @NonNull
    @Column(name = "user_name", nullable = false)
    private String name;

    @NonNull
    @Column(name = "email", nullable = false, length = 200, unique = true)
    private String email;
}