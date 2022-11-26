package colfume.domain.member.service.exception;

import colfume.common.ColfumeException;
import colfume.common.enums.ErrorCode;

public class PasswordSameException extends ColfumeException {

    public PasswordSameException() {
        super(ErrorCode.PASSWORD_SAME);
    }
}