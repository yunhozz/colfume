package colfume.domain.perfume.model.repository.dto;

import colfume.common.enums.ColorType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ColorQueryDto {

    private Long id;
    @JsonIgnore
    private Long perfumeId;
    private ColorType colorType;

    @QueryProjection
    public ColorQueryDto(Long id, Long perfumeId, ColorType colorType) {
        this.id = id;
        this.perfumeId = perfumeId;
        this.colorType = colorType;
    }
}