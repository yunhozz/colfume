package colfume.domain.member.service.exception;

import colfume.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class RefreshTokenNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public RefreshTokenNotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}