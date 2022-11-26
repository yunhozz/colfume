package colfume.common.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Response {

    private Boolean success;
    private HttpStatus status;
    private Result result;

    public static Response success(HttpStatus status) {
        return new Response(true, status, null);
    }

    public static <T> Response success(T data, HttpStatus status) {
        return new Response(true, status, new Success<>(data));
    }

    public static <T> Response failure(T data, HttpStatus status) {
        return new Response(false, status, new Failure<>(data));
    }

    public static <T> Response failure(T data, String message, HttpStatus status) {
        return new Response(false, status, new Failure<>(data, message));
    }
}