package colfume.domain.evaluation.service.exception;

import colfume.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class AlreadyDeletedException extends RuntimeException {

    private final ErrorCode errorCode;

    public AlreadyDeletedException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}