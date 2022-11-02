package colfume.domain.evaluation.service.dto;

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
}