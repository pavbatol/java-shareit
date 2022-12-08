package ru.practicum.shareit.item.comment.model;

import org.mapstruct.Mapping;
import ru.practicum.shareit.common.Mapper;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.model.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

@org.mapstruct.Mapper(componentModel = "spring", uses = {UserMapper.class, ItemMapper.class})
public interface CommentMapper extends Mapper<Comment, CommentDto> {

//    @Mapping(target = "id", source = "dto.id")
//    @Mapping(target = "item.id", source = "itemId")
//    @Mapping(target = "author.id", source = "authorId")
//    @Mapping(target = "created", source = "created")
//    Comment toEntity(CommentDto dto, Long itemId, Long authorId, LocalDateTime created);

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "item.id", source = "itemId")
    @Mapping(target = "author", source = "authorId")
    @Mapping(target = "created", source = "created")
    Comment toEntityFilledAuthor(CommentDto dto, Long itemId, Long authorId, LocalDateTime created);

//    @Mapping(target = "id", source = "dto.id")
//    @Mapping(target = "item", source = "itemId")
//    @Mapping(target = "author", source = "authorId")
//    @Mapping(target = "created", source = "created")
//    Comment toEntityFilledRelations(CommentDto dto, Long itemId, Long authorId, LocalDateTime created);

    @Mapping(target = "authorName", source = "author.name")
    CommentShortDto toShortDto(Comment entity);

    List<CommentShortDto> toShortDtos(List<Comment> entities);
}
