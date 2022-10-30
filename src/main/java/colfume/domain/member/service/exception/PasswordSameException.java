package colfume.domain.member.service.exception;

import colfume.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class PasswordSameException extends RuntimeException {

    private final ErrorCode errorCode;

    public PasswordSameException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
