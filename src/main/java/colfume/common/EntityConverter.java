package colfume.common;

public interface EntityConverter<E, RQ, RS> {

    E convertToEntity(RQ requestDto);
    RS convertToDto(E entity);
}