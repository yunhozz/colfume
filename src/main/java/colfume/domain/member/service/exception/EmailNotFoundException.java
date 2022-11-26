package colfume.domain.member.service.exception;

import colfume.common.ColfumeException;
import colfume.common.enums.ErrorCode;

public class EmailNotFoundException extends ColfumeException {

    public EmailNotFoundException() {
        super(ErrorCode.EMAIL_NOT_FOUND);
    }
}