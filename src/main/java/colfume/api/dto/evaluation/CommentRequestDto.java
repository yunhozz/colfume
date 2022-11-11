package colfume.api.dto.evaluation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {

    @NotBlank(message = "내용을 입력해주세요.")
    @Max(value = 50, message = "최대 50자까지 입력이 가능합니다.")
    private String content;
}