package colfume.dto;

import colfume.domain.perfume.model.entity.Perfume;
import colfume.enums.Color;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static colfume.dto.HashtagDto.*;

public class PerfumeDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerfumeRequestDto {

        @NotBlank(message = "제품명을 입력해주세요.")
        private String name;

        @NotNull(message = "용량을 입력해주세요.")
        private Integer volume;

        @NotNull(message = "가격을 입력해주세요.")
        private Integer price;

        @NotNull(message = "색깔을 한가지 이상 선택해주세요.")
        private List<Color> colors;

        private List<String> moods;

        private List<String> styles;

        private List<String> notes;

        @NotBlank(message = "설명을 입력해주세요.")
        private String description;

        private String imageUrl;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateInfoRequestDto {

        @NotBlank(message = "제품명을 입력해주세요.")
        private String name;

        @NotNull(message = "가격을 입력해주세요.")
        private Integer price;

        @NotBlank(message = "설명을 입력해주세요.")
        private String description;
    }

    @Getter
    @NoArgsConstructor
    public static class PerfumeResponseDto {

        private Long id;
        private String name;
        private Integer volume;
        private Integer price;
        private List<Color> colors;
        private List<String> moods;
        private List<String> styles;
        private List<String> notes;
        private String description;
        private String imageUrl;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;
        private List<HashtagResponseDto> hashtags;

        public PerfumeResponseDto(Perfume perfume) {
            id = perfume.getId();
            name = perfume.getName();
            volume = perfume.getVolume();
            price = perfume.getPrice();
            colors = perfume.getColors();
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
        }
    }

    @Getter
    @NoArgsConstructor
    public static class PerfumeSimpleResponseDto {

        // perfume
        private Long id;
        private String name;
        private Integer volume;
        private Integer price;
        private List<Color> colors;
        private String imageUrl;

        // hashtag
        private List<HashtagSimpleResponseDto> hashtags;

        public void setHashtags(List<HashtagSimpleResponseDto> hashtags) {
            this.hashtags = hashtags;
        }

        @QueryProjection
        public PerfumeSimpleResponseDto(Long id, String name, Integer volume, Integer price, List<Color> colors, String imageUrl) {
            this.id = id;
            this.name = name;
            this.volume = volume;
            this.price = price;
            this.colors = colors;
            this.imageUrl = imageUrl;
        }
    }
}