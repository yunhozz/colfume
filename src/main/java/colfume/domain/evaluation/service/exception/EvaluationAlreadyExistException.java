package colfume.domain.evaluation.service.exception;

import colfume.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class EvaluationAlreadyExistException extends RuntimeException {

    private final ErrorCode errorCode;

    public EvaluationAlreadyExistException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}