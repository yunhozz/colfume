package colfume.common.dto;

import colfume.domain.evaluation.model.entity.Evaluation;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class EvaluationDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EvaluationRequestDto {

        @NotBlank(message = "내용을 입력해주세요.")
        private String content;

        @NotNull(message = "별점을 입력해주세요.")
        private Integer score;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EvaluationResponseDto {

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

    @Getter
    @NoArgsConstructor
    public static class EvaluationQueryDto {

        // evaluation
        private Long id;
        private String content;
        private Integer score;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;

        // member
        private Long userId;
        private String name;
        private String imageUrl;

        @QueryProjection
        public EvaluationQueryDto(Long id, String content, Integer score, LocalDateTime createdDate, LocalDateTime lastModifiedDate, Long userId, String name, String imageUrl) {
            this.id = id;
            this.content = content;
            this.score = score;
            this.createdDate = createdDate;
            this.lastModifiedDate = lastModifiedDate;
            this.userId = userId;
            this.name = name;
            this.imageUrl = imageUrl;
        }
    }
}