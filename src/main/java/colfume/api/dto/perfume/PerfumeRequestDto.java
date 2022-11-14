package colfume.api.dto.perfume;

import colfume.common.enums.ColorType;
import colfume.domain.perfume.model.entity.Note;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfumeRequestDto {

    @NotBlank(message = "제품명을 입력해주세요.")
    private String name;

    @NotNull(message = "용량을 입력해주세요.")
    private Integer volume;

    @NotNull(message = "가격을 입력해주세요.")
    private Integer price;

    @NotEmpty(message = "적어도 하나의 무드를 입력해주세요.")
    private List<String> moods;

    @NotEmpty(message = "적어도 하나의 스타일을 입력해주세요.")
    private List<String> styles;

    @NotEmpty(message = "적어도 하나의 노트를 입력해주세요.")
    private Map<Note.Stage, String> notes;

    @NotBlank(message = "설명을 입력해주세요.")
    private String description;

    private String imageUrl;

    @NotEmpty(message = "최소 하나의 해시태그를 입력해주세요.")
    private List<String> tags;

    @NotEmpty(message = "최소 하나의 색깔을 입력해주세요.")
    private List<ColorType> colorType;
}