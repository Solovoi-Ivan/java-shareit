package ru.practicum.shareit.requests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {
    @Query("select i from ItemRequest i where (i.requester.id = ?1)")
    List<ItemRequest> findByRequester(int requesterId);
}
