package colfume.domain.evaluation.service.exception;

import colfume.common.ColfumeException;
import colfume.common.enums.ErrorCode;

public class CrudNotAuthenticationException extends ColfumeException {

    public CrudNotAuthenticationException() {
        super(ErrorCode.NOT_AUTHENTICATED);
    }
}