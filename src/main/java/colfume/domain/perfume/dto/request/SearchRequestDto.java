package colfume.domain.perfume.dto.request;

import colfume.common.enums.SearchCondition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequestDto {

    private String keyword;
    private SearchCondition condition; // LATEST, ACCURACY
}