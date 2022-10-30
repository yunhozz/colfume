package colfume.domain.notification.service.exception;

import colfume.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class NotificationSendFailException extends RuntimeException {

    private final ErrorCode errorCode;

    public NotificationSendFailException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
