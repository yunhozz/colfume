package colfume.domain.perfume.model.repository.dto;

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

    // hashtag
    private List<HashtagResponseDto> hashtags;

    // color
    private List<ColorResponseDto> colors;

    @QueryProjection
    public PerfumeSimpleQueryDto(Long id, String name, Integer volume, Integer price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.volume = volume;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public void setHashtags(List<HashtagResponseDto> hashtags) {
        this.hashtags = hashtags;
    }

    public void setColors(List<ColorResponseDto> colors) {
        this.colors = colors;
    }
}