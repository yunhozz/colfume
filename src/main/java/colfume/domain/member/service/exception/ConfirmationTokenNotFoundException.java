package colfume.domain.member.service.exception;

import colfume.common.ColfumeException;
import colfume.common.enums.ErrorCode;

public class ConfirmationTokenNotFoundException extends ColfumeException {

    public ConfirmationTokenNotFoundException() {
        super(ErrorCode.CONFIRMATION_TOKEN_NOT_FOUND);
    }
}