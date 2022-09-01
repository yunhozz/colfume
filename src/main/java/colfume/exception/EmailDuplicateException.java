package colfume.exception;

import colfume.enums.ErrorCode;
import lombok.Getter;

@Getter
public class EmailDuplicateException extends RuntimeException {

    private final ErrorCode errorCode;

    public EmailDuplicateException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
