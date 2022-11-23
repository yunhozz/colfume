package colfume.domain.bookmark.model.repository;

import colfume.domain.bookmark.dto.BookmarkQueryDto;

import java.util.List;

public interface BookmarkRepositoryCustom {

    List<BookmarkQueryDto> findBookmarkListByUserId(Long userId);
}