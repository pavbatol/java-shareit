package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwner_IdOrderByIdAsc(Long userId);

    @Query("select i from Item i " +
            "where " +
            "upper(i.name) like upper(concat('%', :text, '%')) " +
            "or " +
            "upper(i.description) like upper(concat('%', :text, '%'))" +
            "and " +
            "i.available = true")
    List<Item> searchByNameOrDescription(String text);
}
