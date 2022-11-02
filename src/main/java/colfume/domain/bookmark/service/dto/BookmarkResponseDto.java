package colfume.domain.bookmark.service.dto;

import colfume.domain.bookmark.model.entity.Bookmark;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BookmarkResponseDto {

    private Long id;
    private Long userId;
    private Long perfumeId;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public BookmarkResponseDto(Bookmark bookmark) {
        id = bookmark.getId();
        userId = bookmark.getMember().getId();
        perfumeId = bookmark.getPerfume().getId();
        createdDate = bookmark.getCreatedDate();
        lastModifiedDate = bookmark.getLastModifiedDate();
    }
}