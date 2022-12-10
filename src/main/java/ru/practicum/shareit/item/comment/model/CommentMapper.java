package ru.practicum.shareit.item.comment.model;

import org.mapstruct.Context;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.shareit.common.GroupService;
import ru.practicum.shareit.common.Mapper;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

@org.mapstruct.Mapper(componentModel = "spring", uses = {UserMapper.class, ItemMapper.class})
public interface CommentMapper extends Mapper<Comment, CommentDto> {

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "item.id", source = "itemId")
    @Mapping(target = "author", source = "authorId", qualifiedByName = "userDyTd")
    @Mapping(target = "created", source = "created")
    Comment toEntityFilledAuthor(CommentDto dto, Long itemId, Long authorId, LocalDateTime created,
                                 @Context GroupService groupService);

    @Named("userDyTd")
    default User setUser(Long id, @Context GroupService groupService) {
        return groupService.getUser(id);
    }

    @Mapping(target = "authorName", source = "author.name")
    CommentShortDto toShortDto(Comment entity);

    List<CommentShortDto> toShortDtos(List<Comment> entities);
}
