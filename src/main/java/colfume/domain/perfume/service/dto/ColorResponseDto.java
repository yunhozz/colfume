package colfume.domain.perfume.service.dto;

import colfume.common.enums.ColorType;
import colfume.domain.perfume.model.entity.Color;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ColorResponseDto {

    private Long id;
    private Long perfumeId;
    private ColorType colorType;

    public ColorResponseDto(Color color) {
        id = color.getId();
        colorType = color.getColorType();
    }
}