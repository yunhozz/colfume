package colfume.domain.perfume.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PerfumeResponseDto {

    private Long id;
    private String name;
    private Integer volume;
    private Integer price;
    private List<String> moods;
    private List<String> styles;
    private Map<String, String> notes;
    private String description;
    private String imageUrl;
    private int numOfLikes;
    private int evaluationCount;
    private double score;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private List<HashtagResponseDto> hashtags;
    private List<ColorResponseDto> colors;
}