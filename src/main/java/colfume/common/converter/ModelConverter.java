package colfume.common.converter;

public interface ModelConverter<I, O> {

    O convertToObject(I source);
    I convertFromObject(O object);
}