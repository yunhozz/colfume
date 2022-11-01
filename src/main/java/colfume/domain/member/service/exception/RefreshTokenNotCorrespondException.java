package colfume.domain.member.service.exception;

import colfume.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class RefreshTokenNotCorrespondException extends RuntimeException {

    private final ErrorCode errorCode;

    public RefreshTokenNotCorrespondException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}