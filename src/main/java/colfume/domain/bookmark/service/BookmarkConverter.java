package colfume.domain.bookmark.service;

import colfume.common.EntityConverter;
import colfume.domain.bookmark.dto.BookmarkResponseDto;
import colfume.domain.bookmark.model.Bookmark;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkConverter implements EntityConverter<Bookmark, Void, BookmarkResponseDto> {

    @Override
    public Bookmark convertToEntity(Void v) {
        return null;
    }

    @Override
    public BookmarkResponseDto convertToDto(Bookmark bookmark) {
        return new BookmarkResponseDto(
                bookmark.getId(),
                bookmark.getMember().getId(),
                bookmark.getPerfume().getId(),
                bookmark.getRedirectUrl(),
                bookmark.getCreatedDate()
        );
    }
}