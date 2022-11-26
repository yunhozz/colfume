package colfume.domain.evaluation.service.exception;

import colfume.common.ColfumeException;
import colfume.common.enums.ErrorCode;

public class EvaluationAlreadyDeletedException extends ColfumeException {

    public EvaluationAlreadyDeletedException() {
        super(ErrorCode.EVALUATION_ALREADY_DELETED);
    }
}