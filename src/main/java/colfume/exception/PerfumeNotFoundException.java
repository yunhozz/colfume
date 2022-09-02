package colfume.exception;

import colfume.enums.ErrorCode;
import lombok.Getter;

@Getter
public class PerfumeNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public PerfumeNotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
