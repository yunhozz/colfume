package colfume.domain.member.service.exception;

import colfume.common.ColfumeException;
import colfume.common.enums.ErrorCode;

public class RefreshTokenNotCorrespondException extends ColfumeException {

    public RefreshTokenNotCorrespondException() {
        super(ErrorCode.REFRESH_TOKEN_NOT_CORRESPOND);
    }
}