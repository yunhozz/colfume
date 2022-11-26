package colfume.common;

import colfume.common.dto.ErrorResponseDto;
import colfume.common.dto.Response;
import colfume.common.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static colfume.common.dto.ErrorResponseDto.NotValidResponseDto;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Response handleRuntimeException(RuntimeException e) {
        // Security 와 관련된 Exception 발생 이전에 RuntimeException 이 먼저 발생하므로 제외
        if (e instanceof AccessDeniedException) {
            return failure(e, ErrorCode.FORBIDDEN);
        }

        if (e instanceof AuthenticationException) {
            return failure(e, ErrorCode.UNAUTHORIZED);
        }

        return failure(e, ErrorCode.INTER_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalStateException.class)
    public Response handleIllegalStateException(IllegalStateException e) {
        return failure(e, ErrorCode.NOT_VALID);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        List<NotValidResponseDto> notValidResponseDtoList = new ArrayList<>() {{
            fieldErrors.forEach(fieldError -> {
                NotValidResponseDto notValidResponseDto = NotValidResponseDto.builder()
                        .field(fieldError.getField())
                        .code(fieldError.getCode())
                        .rejectValue(fieldError.getRejectedValue())
                        .defaultMessage(fieldError.getDefaultMessage())
                        .build();

                add(notValidResponseDto);
            });
        }};

        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ErrorCode.NOT_VALID, notValidResponseDtoList);
        return Response.failure(errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }

    @ExceptionHandler(ColfumeException.class)
    public Response handleColfumeException(ColfumeException e) {
        return failure(e, e.getErrorCode());
    }

    private Response failure(Exception e, ErrorCode errorCode) {
        log.error(String.format("handle%s", e.getClass().getSimpleName()));
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(errorCode);
        return Response.failure(errorResponseDto, HttpStatus.valueOf(errorResponseDto.getStatus()));
    }
}