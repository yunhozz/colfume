package colfume.domain.perfume.dto.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PerfumeSearchQueryDto {

    private Long id;
    private String name;
    private String description;
    private String mood;
    private String style;
    private String note;
    private String tag;

    @QueryProjection
    public PerfumeSearchQueryDto(Long id, String name, String description, String mood, String style, String note, String tag) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.mood = mood;
        this.style = style;
        this.note = note;
        this.tag = tag;
    }
}
