package colfume.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Failure<T> implements Result {

    private String message;
    private T data;
}