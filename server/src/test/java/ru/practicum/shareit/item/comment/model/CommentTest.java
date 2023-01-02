package ru.practicum.shareit.item.comment.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    @Test
    void testEquals() {
        Comment comment = new Comment();
        Comment comment2 = new Comment();
        assertFalse(comment.equals(null));
        assertTrue(comment.equals(comment));
        assertFalse(comment.equals(comment2));
    }

    @Test
    void testHashCode() {
        Comment comment = new Comment();
        int code = comment.hashCode();
        assertEquals(comment.getClass().hashCode(), code);
    }
}