package colfume.domain.evaluation.service.exception;

import colfume.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class CommentNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public CommentNotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}