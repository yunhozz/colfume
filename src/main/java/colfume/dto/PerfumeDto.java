package colfume.dto;

import colfume.domain.perfume.model.entity.Perfume;
import colfume.enums.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class PerfumeDto {

    @Getter
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
    @AllArgsConstructor
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
        }
    }
}
