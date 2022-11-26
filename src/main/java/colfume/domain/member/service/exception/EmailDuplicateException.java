package colfume.domain.member.service.exception;

import colfume.common.ColfumeException;
import colfume.common.enums.ErrorCode;

public class EmailDuplicateException extends ColfumeException {

    public EmailDuplicateException() {
        super(ErrorCode.EMAIL_DUPLICATE);
    }
}