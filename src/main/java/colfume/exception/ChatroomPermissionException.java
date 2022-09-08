package colfume.exception;

import colfume.enums.ErrorCode;
import lombok.Getter;

@Getter
public class ChatroomPermissionException extends RuntimeException {

    private final ErrorCode errorCode;

    public ChatroomPermissionException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
