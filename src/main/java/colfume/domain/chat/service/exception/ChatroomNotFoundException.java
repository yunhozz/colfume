package colfume.domain.chat.service.exception;

import colfume.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class ChatroomNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public ChatroomNotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
