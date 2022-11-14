package colfume.domain.evaluation.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {

    private Long id;
    private Long writerId;
    private Long evaluationId;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}