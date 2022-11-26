package colfume.domain.bookmark.service.exception;

import colfume.common.ColfumeException;
import colfume.common.enums.ErrorCode;

public class BookmarkAlreadyCreatedException extends ColfumeException {

    public BookmarkAlreadyCreatedException() {
        super(ErrorCode.BOOKMARK_ALREADY_CREATED);
    }
}