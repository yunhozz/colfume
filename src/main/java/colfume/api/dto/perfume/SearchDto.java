package colfume.api.dto.perfume;

import colfume.common.enums.SearchCondition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchDto {

    private String keyword;
    private SearchCondition condition; // LATEST, ACCURACY
}