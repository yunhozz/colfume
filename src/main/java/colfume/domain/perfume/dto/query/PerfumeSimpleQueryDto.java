package colfume.domain.perfume.dto.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PerfumeSimpleQueryDto {

    // perfume
    private Long id;
    private String name;
    private Integer volume;
    private Integer price;
    private String imageUrl;
    private boolean isBookmarked;

    // hashtag
    private List<HashtagQueryDto> hashtags;

    // color
    private List<ColorQueryDto> colors;

    @QueryProjection
    public PerfumeSimpleQueryDto(Long id, String name, Integer volume, Integer price, String imageUrl, boolean isBookmarked) {
        this.id = id;
        this.name = name;
        this.volume = volume;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isBookmarked = isBookmarked;
    }

    public void setHashtags(List<HashtagQueryDto> hashtags) {
        this.hashtags = hashtags;
    }

    public void setColors(List<ColorQueryDto> colors) {
        this.colors = colors;
    }
}