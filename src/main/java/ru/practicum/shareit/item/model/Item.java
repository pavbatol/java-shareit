package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.Objects;

/**
 * TODO Sprint add-controllers.
 */

@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    Long id;

    @Column(name = "item_name", nullable = false)
    String name;

    @Column(name = "item_description", nullable = false)
    String description;

    @Column(nullable = false)
    Boolean available;

    @Column(name = "request_id")
    Long requestId;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    User owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Item item = (Item) o;
        return id != null && Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}


