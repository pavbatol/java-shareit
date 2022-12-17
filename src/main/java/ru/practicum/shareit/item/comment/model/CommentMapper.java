package ru.practicum.shareit.item.comment.model;

import org.mapstruct.Context;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.shareit.common.Mapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.validator.ValidatorManager.getNonNullObject;

@org.mapstruct.Mapper(componentModel = "spring")
public interface CommentMapper extends Mapper<Comment, CommentDto> {

    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "item.id", source = "itemId")
    @Mapping(target = "author", source = "authorId", qualifiedByName = "userById")
    @Mapping(target = "created", source = "created")
    Comment toEntityFilledAuthor(CommentDto dto, Long itemId, Long authorId, LocalDateTime created,
                                 @Context UserRepository userRepository);

    @Named("userById")
    default User idToUser(Long id, @Context UserRepository userRepository) {
        return getNonNullObject(userRepository, id);
    }

    @Mapping(target = "authorName", source = "author.name")
    CommentDtoShort toShortDto(Comment entity);

    List<CommentDtoShort> toShortDtos(List<Comment> entities);
}
