package colfume.exception;

import colfume.enums.ErrorCode;
import lombok.Getter;

@Getter
public class EmailNotVerifiedException extends RuntimeException {

    private final ErrorCode errorCode;

    public EmailNotVerifiedException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}