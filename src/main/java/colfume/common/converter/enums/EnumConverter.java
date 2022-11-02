package colfume.common.converter.enums;

public interface EnumConverter<E, V> {

    E convertToEnum(V value);
    V convertToValue(E enums);
}