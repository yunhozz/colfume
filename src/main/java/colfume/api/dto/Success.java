package colfume.api.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Success<T> implements Result {

    private T data;
}