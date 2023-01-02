package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    @Test
    void testEquals() {
        Booking booking = new Booking();
        Booking booking2 = new Booking();
        assertFalse(booking.equals(null));
        assertTrue(booking.equals(booking));
        assertFalse(booking.equals(booking2));
    }

    @Test
    void testHashCode() {
        Booking booking = new Booking();
        int code = booking.hashCode();
        assertEquals(booking.getClass().hashCode(), code);
    }
}