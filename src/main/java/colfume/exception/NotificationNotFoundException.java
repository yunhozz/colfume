package colfume.exception;

import colfume.enums.ErrorCode;
import lombok.Getter;

@Getter
public class NotificationNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public NotificationNotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
