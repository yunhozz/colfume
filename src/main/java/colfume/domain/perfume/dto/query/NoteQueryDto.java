package colfume.domain.perfume.dto.query;

import colfume.domain.perfume.model.entity.Note;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoteQueryDto {

    @JsonIgnore
    private Long perfumeId;
    private Note.Stage stage;
    private String note;

    @QueryProjection
    public NoteQueryDto(Long perfumeId, Note.Stage stage, String note) {
        this.perfumeId = perfumeId;
        this.stage = stage;
        this.note = note;
    }
}