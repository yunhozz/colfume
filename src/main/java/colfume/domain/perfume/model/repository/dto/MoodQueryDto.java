package colfume.domain.perfume.model.repository.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MoodQueryDto {

    @JsonIgnore
    private Long perfumeId;
    private String mood;

    @QueryProjection
    public MoodQueryDto(Long perfumeId, String mood) {
        this.perfumeId = perfumeId;
        this.mood = mood;
    }
}