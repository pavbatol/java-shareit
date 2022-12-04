package ru.practicum.shareit.booking.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * TODO Sprint add-bookings.
 */

@Setter
@Getter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    Long id;

    @ManyToOne
    @JoinColumn(name = "booker_id", nullable = false)
    User booker;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    Item item;

    @Column(name = "start_time", nullable = false)
    LocalDateTime start;

    @Column(name = "end_time", nullable = false)
    LocalDateTime end;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    BookingStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Booking booking = (Booking) o;
        return id != null && Objects.equals(id, booking.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
