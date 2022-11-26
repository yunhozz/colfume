package colfume.common;

import colfume.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class ColfumeException extends RuntimeException {

    private final ErrorCode errorCode;

    public ColfumeException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}