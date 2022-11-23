package colfume.domain.bookmark.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookmarkQueryDto {

    // bookmark
    private Long id;

    // perfume
    private Long perfumeId;
    private String name;
    private String imageUrl;

    @QueryProjection
    public BookmarkQueryDto(Long id, Long perfumeId, String name, String imageUrl) {
        this.id = id;
        this.perfumeId = perfumeId;
        this.name = name;
        this.imageUrl = imageUrl;
    }
}