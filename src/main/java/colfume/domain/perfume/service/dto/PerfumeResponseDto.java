package colfume.domain.perfume.service.dto;

import colfume.domain.perfume.model.entity.Perfume;
import colfume.domain.perfume.model.repository.dto.ColorResponseDto;
import colfume.domain.perfume.model.repository.dto.HashtagResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<String> notes;
    private String description;
    private String imageUrl;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private List<HashtagResponseDto> hashtags;
    private List<ColorResponseDto> colors;

    public PerfumeResponseDto(Perfume perfume) {
        id = perfume.getId();
        name = perfume.getName();
        volume = perfume.getVolume();
        price = perfume.getPrice();
        moods = perfume.getMoods();
        styles = perfume.getStyles();
        notes = perfume.getNotes();
        description = perfume.getDescription();
        imageUrl = perfume.getImageUrl();
        createdDate = perfume.getCreatedDate();
        lastModifiedDate = perfume.getLastModifiedDate();
        hashtags = perfume.getHashtags().stream()
                .map(HashtagResponseDto::new)
                .collect(Collectors.toList());
        colors = perfume.getColors().stream()
                .map(ColorResponseDto::new)
                .collect(Collectors.toList());
    }
}