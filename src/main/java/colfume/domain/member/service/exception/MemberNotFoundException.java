package colfume.domain.member.service.exception;

import colfume.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class MemberNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public MemberNotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}