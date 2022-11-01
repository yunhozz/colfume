package colfume.domain.bookmark.model.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BookmarkQueryDto {

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