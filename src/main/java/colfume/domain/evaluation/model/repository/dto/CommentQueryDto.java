package colfume.domain.evaluation.model.repository.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentQueryDto {

    // comment
    private Long id;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    // evaluation
    @JsonIgnore
    private Long evaluationId;

    // member
    private Long writerId;
    private String name;
    private String imageUrl;

    @QueryProjection
    public CommentQueryDto(Long id, String content, LocalDateTime createdDate, LocalDateTime lastModifiedDate, Long evaluationId, Long writerId, String name, String imageUrl) {
        this.id = id;
        this.content = content;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.evaluationId = evaluationId;
        this.writerId = writerId;
        this.name = name;
        this.imageUrl = imageUrl;
    }
}