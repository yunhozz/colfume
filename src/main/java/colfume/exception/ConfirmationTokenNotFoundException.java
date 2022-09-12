package colfume.exception;

import colfume.enums.ErrorCode;
import lombok.Getter;

@Getter
public class ConfirmationTokenNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public ConfirmationTokenNotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
