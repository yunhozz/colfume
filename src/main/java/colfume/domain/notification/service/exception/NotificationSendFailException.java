package colfume.domain.notification.service.exception;

import colfume.common.ColfumeException;
import colfume.common.enums.ErrorCode;

public class NotificationSendFailException extends ColfumeException {

    public NotificationSendFailException() {
        super(ErrorCode.NOTIFICATION_SEND_FAIL);
    }
}