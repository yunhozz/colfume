package colfume.domain.perfume.model.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PerfumeSearchQueryDto {

    private Long id;
    private String name;
    private String description;
    private String tag;

    @QueryProjection
    public PerfumeSearchQueryDto(Long id, String name, String description, String tag) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tag = tag;
    }
}
