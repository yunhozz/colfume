package colfume.domain.chat.service.exception;

import colfume.common.ColfumeException;
import colfume.common.enums.ErrorCode;

public class ChatroomNotFoundException extends ColfumeException {

    public ChatroomNotFoundException() {
        super(ErrorCode.CHATROOM_NOT_FOUND);
    }
}