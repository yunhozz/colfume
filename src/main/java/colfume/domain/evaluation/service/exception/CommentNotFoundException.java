package colfume.domain.evaluation.service.exception;

import colfume.common.ColfumeException;
import colfume.common.enums.ErrorCode;

public class CommentNotFoundException extends ColfumeException {

    public CommentNotFoundException() {
        super(ErrorCode.COMMENT_NOT_FOUND);
    }
}