package ru.practicum.shareit.item.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findAllByOwnerId(Long ownerId, Pageable pageable);

    @Query("select i from Item i " +
            "where " +
            "upper(i.name) like upper(concat('%', :text, '%')) " +
            "or " +
            "upper(i.description) like upper(concat('%', :text, '%'))" +
            "and " +
            "i.available = true")
    Page<Item> searchByNameOrDescription(String text, Pageable pageable);
}
