package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestTest {

    @Test
    void testEquals() {
        ItemRequest itemRequest = new ItemRequest();
        ItemRequest itemRequest2 = new ItemRequest();
        assertFalse(itemRequest.equals(null));
        assertTrue(itemRequest.equals(itemRequest));
        assertFalse(itemRequest.equals(itemRequest2));
    }

    @Test
    void testHashCode() {
        ItemRequest itemRequest = new ItemRequest();
        int code = itemRequest.hashCode();
        assertEquals(itemRequest.getClass().hashCode(), code);
    }
}