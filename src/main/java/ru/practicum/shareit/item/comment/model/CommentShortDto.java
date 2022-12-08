package ru.practicum.shareit.item.comment.model;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class CommentShortDto {

    Long id;

    String text;

    String authorName;

    LocalDateTime created;
}