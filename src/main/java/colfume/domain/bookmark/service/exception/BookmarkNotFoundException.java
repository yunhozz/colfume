package colfume.domain.bookmark.service.exception;

import colfume.common.ColfumeException;
import colfume.common.enums.ErrorCode;

public class BookmarkNotFoundException extends ColfumeException {

    public BookmarkNotFoundException() {
        super(ErrorCode.BOOKMARK_NOT_FOUND);
    }
}