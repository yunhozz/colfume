package colfume.domain.member.service.exception;

import colfume.common.ColfumeException;
import colfume.common.enums.ErrorCode;

public class EmailNotVerifiedException extends ColfumeException {

    public EmailNotVerifiedException() {
        super(ErrorCode.EMAIL_NOT_VERIFIED);
    }
}