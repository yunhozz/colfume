package colfume.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Failure<T> implements Result {

    private final T data;
    private String message;

    public Failure(T data) {
        this.data = data;
    }

    public Failure(T data, String message) {
        this.data = data;
        this.message = message;
    }
}