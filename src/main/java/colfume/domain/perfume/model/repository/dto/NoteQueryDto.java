package colfume.domain.perfume.model.repository.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoteQueryDto {

    @JsonIgnore
    private Long perfumeId;
    private String note;

    @QueryProjection
    public NoteQueryDto(Long perfumeId, String note) {
        this.perfumeId = perfumeId;
        this.note = note;
    }
}