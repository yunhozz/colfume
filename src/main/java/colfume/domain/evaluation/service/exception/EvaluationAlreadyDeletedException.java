package colfume.domain.evaluation.service.exception;

import colfume.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class EvaluationAlreadyDeletedException extends RuntimeException {

    private final ErrorCode errorCode;

    public EvaluationAlreadyDeletedException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}