package colfume.domain.chat.service.exception;

import colfume.common.ColfumeException;
import colfume.common.enums.ErrorCode;

public class ChatroomPermissionException extends ColfumeException {

    public ChatroomPermissionException() {
        super(ErrorCode.CHATROOM_NOT_PERMISSION);
    }
}