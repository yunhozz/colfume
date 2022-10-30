package colfume.domain.member.service.exception;

import colfume.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class PasswordMismatchException extends RuntimeException {

    private final ErrorCode errorCode;

    public PasswordMismatchException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
