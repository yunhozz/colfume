package colfume.dto;

import colfume.domain.evaluation.model.entity.Evaluation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class EvaluationDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EvaluationRequestDto {

        @NotBlank(message = "내용을 입력해주세요.")
        private String content;
    }

    @Getter
    @NoArgsConstructor
    public static class EvaluationResponseDto {

        private Long id;
        private Long writerId;
        private Long perfumeId;
        private String content;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;

        public EvaluationResponseDto(Evaluation evaluation) {
            id = evaluation.getId();
            writerId = evaluation.getWriter().getId();
            perfumeId = evaluation.getPerfume().getId();
            content = evaluation.getContent();
            createdDate = evaluation.getCreatedDate();
            lastModifiedDate = evaluation.getLastModifiedDate();
        }
    }
}