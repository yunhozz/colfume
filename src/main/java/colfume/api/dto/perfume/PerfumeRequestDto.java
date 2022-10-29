package colfume.api.dto.perfume;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

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

    private List<String> moods;

    private List<String> styles;

    private List<String> notes;

    @NotBlank(message = "설명을 입력해주세요.")
    private String description;

    private String imageUrl;
}