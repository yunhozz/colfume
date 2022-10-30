package colfume.domain.perfume.model.repository.dto;

import colfume.domain.perfume.model.entity.Color;
import colfume.common.enums.ColorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ColorResponseDto {

    private Long id;
    private Long perfumeId;
    private ColorType colorType;

    public ColorResponseDto(Color color) {
        id = color.getId();
        colorType = color.getColorType();
    }
}