package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Query("select i from Item i where (lower(i.name) like lower(concat('%', ?1, '%'))" +
            "or lower(i.description) like lower(concat('%', ?1, '%'))) and i.available = true")
    List<Item> search(String text);
}