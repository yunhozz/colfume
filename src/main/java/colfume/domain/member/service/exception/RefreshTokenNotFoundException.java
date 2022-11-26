package colfume.domain.member.service.exception;

import colfume.common.ColfumeException;
import colfume.common.enums.ErrorCode;

public class RefreshTokenNotFoundException extends ColfumeException {

    public RefreshTokenNotFoundException() {
        super(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
    }
}