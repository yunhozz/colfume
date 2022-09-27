package colfume.api.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Failure<T> implements Result {

    private T data;
}