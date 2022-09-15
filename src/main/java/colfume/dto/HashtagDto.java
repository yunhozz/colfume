package colfume.dto;

import colfume.domain.perfume.model.entity.Hashtag;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class HashtagDto {

    @Getter
    @NoArgsConstructor
    public static class HashtagResponseDto {

        private Long id;
        private Long perfumeId;
        private String tag;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;

        public HashtagResponseDto(Hashtag hashtag) {
            id = hashtag.getId();
            perfumeId = hashtag.getPerfume().getId();
            tag = hashtag.getTag();
            createdDate = hashtag.getCreatedDate();
            lastModifiedDate = hashtag.getLastModifiedDate();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class HashtagSimpleResponseDto {

        private Long id;
        private Long perfumeId;
        private String tag;

        public HashtagSimpleResponseDto(Hashtag hashtag) {
            id = hashtag.getId();
            perfumeId = hashtag.getPerfume().getId();
            tag = hashtag.getTag();
        }
    }
}