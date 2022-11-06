package colfume.domain.bookmark.service.exception;

import colfume.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class BookmarkAlreadyCreatedException extends RuntimeException {

    private final ErrorCode errorCode;

    public BookmarkAlreadyCreatedException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}