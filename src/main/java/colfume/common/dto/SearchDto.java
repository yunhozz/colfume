package colfume.common.dto;

import colfume.common.enums.SearchCondition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchDto {

    @NotBlank(message = "키워드를 입력해주세요.")
    private String keyword;

    @NotNull
    private SearchCondition condition; // LATEST, ACCURACY
}