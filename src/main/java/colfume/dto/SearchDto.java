package colfume.dto;

import colfume.enums.ColorType;
import colfume.enums.SearchCondition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchDto {

    // 향수 각 필드 상세 조회
    private Integer volumeGoe; // 최소 용량
    private Integer volumeLoe; // 최대 용량
    private Integer priceGoe; // 최소 가격
    private Integer priceLoe; // 최대 가격
    private List<ColorType> colorTypes; // 색깔

    // 페이징 순서
    private SearchCondition condition; // LATEST, POPULARITY
}