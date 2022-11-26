package colfume.domain.notification.service.exception;

import colfume.common.ColfumeException;
import colfume.common.enums.ErrorCode;

public class NotificationNotFoundException extends ColfumeException {

    public NotificationNotFoundException() {
        super(ErrorCode.NOTIFICATION_NOT_FOUND);
    }
}