package colfume.domain.bookmark.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkResponseDto {

    private Long id;
    private Long userId;
    private Long perfumeId;
    private String redirectUrl;
    private Boolean isDeleted;
    private LocalDateTime createdDate;
}