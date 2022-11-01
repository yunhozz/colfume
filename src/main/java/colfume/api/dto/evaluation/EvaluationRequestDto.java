package colfume.api.dto.evaluation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationRequestDto {

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "별점을 입력해주세요.")
    private Integer score;
}