package colfume.common.dto;

import colfume.common.enums.ColorType;
import colfume.common.enums.SortCondition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SortDto {

    // 향수 각 필드 상세 조회
    private Integer volumeGoe; // 최소 용량
    private Integer volumeLoe; // 최대 용량
    private Integer priceGoe; // 최소 가격
    private Integer priceLoe; // 최대 가격
    private List<ColorType> colorTypes; // 색깔

    // 페이징 순서
    private SortCondition condition; // LATEST, POPULARITY
}