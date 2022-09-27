package colfume.api.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Response {

    private boolean success;
    private int code;
    private Result result;
    private HttpStatus status;

    public static Response success(HttpStatus status) {
        return new Response(true, 0, null, status);
    }

    public static <T> Response success(T data, HttpStatus status) {
        return new Response(true, 0, new Success<>(data), status);
    }

    public static Response failure(int code, String message, HttpStatus status) {
        return new Response(false, code, new Failure<>(message), status);
    }

    public static <T> Response failure(int code, T data, HttpStatus status) {
        return new Response(false, code, new Failure<>(data), status);
    }
}