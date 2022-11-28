package colfume.common;

public interface EntityConverter<E, Q, S> {

    E convertToEntity(Q requestDto);
    S convertToDto(E entity);
}