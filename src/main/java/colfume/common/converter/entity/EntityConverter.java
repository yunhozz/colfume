package colfume.common.converter.entity;

public interface EntityConverter<E, RQ, RS> {

    E convertToEntity(RQ requestDto);
    RS convertToDto(E entity);
}