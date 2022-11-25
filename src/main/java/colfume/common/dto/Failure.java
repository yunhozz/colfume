package colfume.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Failure<T> implements Result {

    private final LocalDateTime timestamp;
    private final T data;
    private String message;

    public Failure(T data) {
        timestamp = LocalDateTime.now();
        this.data = data;
    }

    public Failure(T data, String message) {
        timestamp = LocalDateTime.now();
        this.data = data;
        this.message = message;
    }
}