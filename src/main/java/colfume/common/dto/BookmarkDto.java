package colfume.common.dto;

import colfume.domain.bookmark.model.entity.Bookmark;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class BookmarkDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookmarkResponseDto {

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

    @Getter
    @NoArgsConstructor
    public static class BookmarkQueryDto {

        // bookmark
        private Long id;
        private LocalDateTime createdDate;

        // perfume
        private Long perfumeId;
        private String name;
        private String imageUrl;

        // member
        private Long userId;

        @QueryProjection
        public BookmarkQueryDto(Long id, LocalDateTime createdDate, Long perfumeId, String name, String imageUrl, Long userId) {
            this.id = id;
            this.createdDate = createdDate;
            this.perfumeId = perfumeId;
            this.name = name;
            this.imageUrl = imageUrl;
            this.userId = userId;
        }
    }
}