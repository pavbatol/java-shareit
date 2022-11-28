package ru.practicum.shareit.common;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

public interface Mapper<E, D> {

    D toDto(E entity);

    E toEntity(D dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    E toEntity(D dto, @MappingTarget E targetEntity);

    List<D> toDtos(List<E> entities);
}
