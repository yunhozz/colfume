package colfume.exception;

import colfume.enums.ErrorCode;
import lombok.Getter;

@Getter
public class CrudNotAuthenticationException extends RuntimeException {

    private final ErrorCode errorCode;

    public CrudNotAuthenticationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}