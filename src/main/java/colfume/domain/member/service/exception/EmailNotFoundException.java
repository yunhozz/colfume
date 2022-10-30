package colfume.domain.member.service.exception;

import colfume.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class EmailNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public EmailNotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
