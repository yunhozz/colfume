package colfume.domain.evaluation.model.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class EvaluationQueryDto {

    // evaluation
    private Long id;
    private String content;
    private Double score;
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