package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void testEquals() {
        Item item = new Item();
        Item item2 = new Item();
        assertFalse(item.equals(null));
        assertTrue(item.equals(item));
        assertFalse(item.equals(item2));
    }

    @Test
    void testHashCode() {
        Item item = new Item();
        int code = item.hashCode();
        assertEquals(item.getClass().hashCode(), code);
    }
}