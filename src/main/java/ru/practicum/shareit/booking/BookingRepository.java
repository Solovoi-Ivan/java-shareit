package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("select b from Booking b where (b.item.id = ?1) and (?2 > b.start) and (b.status = 'APPROVED')" +
            "order by b.start desc, 1")
    Booking getLastBooking(int itemId, LocalDateTime now);

    @Query("select b from Booking b where (b.item.id = ?1) and (?2 < b.start) and (b.status = 'APPROVED')" +
            "order by b.start asc, 1")
    Booking getNextBooking(int itemId, LocalDateTime now);
}