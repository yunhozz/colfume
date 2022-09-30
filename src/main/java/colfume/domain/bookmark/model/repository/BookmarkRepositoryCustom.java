package colfume.domain.bookmark.model.repository;

import java.util.List;

import static colfume.dto.BookmarkDto.*;

public interface BookmarkRepositoryCustom {

    List<BookmarkQueryDto> findBookmarkListByUserId(Long userId);
}