package colfume.domain.evaluation.service.exception;

import colfume.common.ColfumeException;
import colfume.common.enums.ErrorCode;

public class EvaluationNotFoundException extends ColfumeException {

    public EvaluationNotFoundException() {
        super(ErrorCode.EVALUATION_NOT_FOUND);
    }
}