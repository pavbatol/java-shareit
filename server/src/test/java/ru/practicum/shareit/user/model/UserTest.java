package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testEquals() {
        User user = new User();
        User user2 = new User();
        assertFalse(user.equals(null));
        assertTrue(user.equals(user));
        assertFalse(user.equals(user2));
    }

    @Test
    void testHashCode() {
        User user = new User();
        int code = user.hashCode();
        assertEquals(user.getClass().hashCode(), code);
    }
}