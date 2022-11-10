package colfume.domain.perfume.model.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PerfumeQueryDto {

    private Long id;
    private String name;
    private Integer volume;
    private Integer price;
    private String description;
    private String imageUrl;
    private int numOfLikes;
    private int evaluationCount;
    private double score;
    private boolean isBookmarked;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    private List<MoodQueryDto> moods;
    private List<StyleQueryDto> styles;
    private List<NoteQueryDto> notes;
    private List<HashtagQueryDto> hashtags;
    private List<ColorQueryDto> colors;

    @QueryProjection
    public PerfumeQueryDto(Long id, String name, Integer volume, Integer price, String description, String imageUrl, int numOfLikes, int evaluationCount, double score, boolean isBookmarked, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.name = name;
        this.volume = volume;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.numOfLikes = numOfLikes;
        this.evaluationCount = evaluationCount;
        this.score = score;
        this.isBookmarked = isBookmarked;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public void setMoods(List<MoodQueryDto> moods) {
        this.moods = moods;
    }

    public void setStyles(List<StyleQueryDto> styles) {
        this.styles = styles;
    }

    public void setNotes(List<NoteQueryDto> notes) {
        this.notes = notes;
    }

    public void setHashtags(List<HashtagQueryDto> hashtags) {
        this.hashtags = hashtags;
    }

    public void setColors(List<ColorQueryDto> colors) {
        this.colors = colors;
    }
}