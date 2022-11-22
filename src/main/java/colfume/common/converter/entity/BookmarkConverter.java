package colfume.common.converter.entity;

import colfume.domain.bookmark.model.entity.Bookmark;
import colfume.domain.bookmark.dto.response.BookmarkResponseDto;
import org.springframework.stereotype.Component;

@Component
public class BookmarkConverter implements EntityConverter<Bookmark, Object, BookmarkResponseDto> {

    @Override
    public Bookmark convertToEntity(Object object) {
        return null;
    }

    @Override
    public BookmarkResponseDto convertToDto(Bookmark bookmark) {
        return new BookmarkResponseDto(
                bookmark.getId(),
                bookmark.getMember().getId(),
                bookmark.getPerfume().getId(),
                bookmark.getRedirectUrl(),
                bookmark.isDeleted(),
                bookmark.getCreatedDate()
        );
    }
}