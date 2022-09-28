package colfume.api.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Response {

    private HttpStatus status;
    private boolean success;
    private int code;
    private Result result;

    public static Response success(HttpStatus status) {
        return new Response(status, true, 0, null);
    }

    public static <T> Response success(T data, HttpStatus status) {
        return new Response(status, true, 0, new Success<>(data));
    }

    public static <T> Response failure(int code, String message, T data, HttpStatus status) {
        return new Response(status, false, code, new Failure<>(message, data));
    }
}