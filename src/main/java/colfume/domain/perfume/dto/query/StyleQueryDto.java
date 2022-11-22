package colfume.domain.perfume.dto.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StyleQueryDto {

    @JsonIgnore
    private Long perfumeId;
    private String style;

    @QueryProjection
    public StyleQueryDto(Long perfumeId, String style) {
        this.perfumeId = perfumeId;
        this.style = style;
    }
}