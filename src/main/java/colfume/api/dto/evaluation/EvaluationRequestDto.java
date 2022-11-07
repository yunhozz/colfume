package colfume.api.dto.evaluation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationRequestDto {

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(min = 10, max = 100, message = "최소 10자, 최대 100자까지 입력이 가능합니다.")
    private String content;

    @NotNull(message = "별점을 입력해주세요.")
    private Double score;
}