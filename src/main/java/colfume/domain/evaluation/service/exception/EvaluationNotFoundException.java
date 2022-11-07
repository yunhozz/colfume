package colfume.domain.evaluation.service.exception;

import colfume.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class EvaluationNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public EvaluationNotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}