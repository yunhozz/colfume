package colfume.domain.evaluation.service.exception;

import colfume.common.ColfumeException;
import colfume.common.enums.ErrorCode;

public class EvaluationAlreadyExistException extends ColfumeException {

    public EvaluationAlreadyExistException() {
        super(ErrorCode.EVALUATION_ALREADY_EXIST);
    }
}