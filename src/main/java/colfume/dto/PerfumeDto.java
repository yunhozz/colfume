package colfume.dto;

import colfume.domain.perfume.model.entity.Color;
import colfume.domain.perfume.model.entity.Hashtag;
import colfume.domain.perfume.model.entity.Perfume;
import colfume.enums.ColorType;
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
    @AllArgsConstructor
    public static class PerfumeResponseDto {

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

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HashtagResponseDto {

        private Long id;
        private Long perfumeId;
        private String tag;

        public HashtagResponseDto(Hashtag hashtag) {
            id = hashtag.getId();
            perfumeId = hashtag.getPerfume().getId();
            tag = hashtag.getTag();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ColorResponseDto {

        private Long id;
        private Long perfumeId;
        private ColorType colorType;

        public ColorResponseDto(Color color) {
            id = color.getId();
            colorType = color.getColorType();
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
        private String imageUrl;

        // hashtag
        private List<HashtagResponseDto> hashtags;

        // color
        private List<ColorResponseDto> colors;

        @QueryProjection
        public PerfumeSimpleResponseDto(Long id, String name, Integer volume, Integer price, String imageUrl) {
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

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerfumeSearchResponseDto {

        private Long id;
        private String name;
        private String description;
        private String tag;
    }
}