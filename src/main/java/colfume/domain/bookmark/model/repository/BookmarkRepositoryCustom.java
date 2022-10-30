package colfume.domain.bookmark.model.repository;

import java.util.List;

import static colfume.common.dto.BookmarkDto.*;

public interface BookmarkRepositoryCustom {

    List<BookmarkQueryDto> findBookmarkListByUserId(Long userId);
}