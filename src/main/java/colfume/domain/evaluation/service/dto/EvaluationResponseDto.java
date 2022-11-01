package colfume.domain.evaluation.service.dto;

import colfume.domain.evaluation.model.entity.Evaluation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationResponseDto {

    private Long id;
    private Long writerId;
    private Long perfumeId;
    private String content;
    private Integer score;
    private Boolean isDeleted;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public EvaluationResponseDto(Evaluation evaluation) {
        id = evaluation.getId();
        writerId = evaluation.getWriter().getId();
        perfumeId = evaluation.getPerfume().getId();
        content = evaluation.getContent();
        score = evaluation.getScore();
        isDeleted = evaluation.isDeleted();
        createdDate = evaluation.getCreatedDate();
        lastModifiedDate = evaluation.getLastModifiedDate();
    }
}