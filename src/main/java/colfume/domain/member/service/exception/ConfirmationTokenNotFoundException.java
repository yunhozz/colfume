package colfume.domain.member.service.exception;

import colfume.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class ConfirmationTokenNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public ConfirmationTokenNotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
