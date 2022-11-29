package colfume.domain.bookmark.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookmarkQueryDto {

    // bookmark
    private Long id;
    private String redirectUrl;

    // perfume
    private Long perfumeId;
    private String name;
    private String imageUrl;

    @QueryProjection
    public BookmarkQueryDto(Long id, String redirectUrl, Long perfumeId, String name, String imageUrl) {
        this.id = id;
        this.redirectUrl = redirectUrl;
        this.perfumeId = perfumeId;
        this.name = name;
        this.imageUrl = imageUrl;
    }
}