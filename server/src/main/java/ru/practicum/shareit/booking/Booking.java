package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.util.BookingStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "BOOKINGS")
@NoArgsConstructor
@RequiredArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private int id;
    @NonNull
    @Column(name = "start_date")
    private LocalDateTime start;
    @NonNull
    @Column(name = "end_date")
    private LocalDateTime end;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker;
    @NonNull
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}