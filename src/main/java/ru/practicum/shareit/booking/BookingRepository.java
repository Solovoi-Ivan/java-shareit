package ru.practicum.shareit.booking;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("select b from Booking b where (b.item.id = ?1) and (?2 > b.start) and (b.status = 'APPROVED')" +
            "order by b.start desc, 1")
    Booking getLastBooking(int itemId, LocalDateTime now);

    @Query("select b from Booking b where (b.item.id = ?1) and (?2 < b.start) and (b.status = 'APPROVED')" +
            "order by b.start asc, 1")
    Booking getNextBooking(int itemId, LocalDateTime now);

    @Query("select b from Booking b where (b.item.owner = ?1)")
    List<Booking> findByOwner(User owner, PageRequest pageRequest);

    List<Booking> findByBooker(User booker, PageRequest pageRequest);
}