package colfume.domain.evaluation.dto.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    private Long writerId;
    private String name;
    private String imageUrl;

    // comment
    private List<CommentQueryDto> comments;

    @QueryProjection
    public EvaluationQueryDto(Long id, String content, Double score, LocalDateTime createdDate, LocalDateTime lastModifiedDate, Long writerId, String name, String imageUrl) {
        this.id = id;
        this.content = content;
        this.score = score;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.writerId = writerId;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public void setComments(List<CommentQueryDto> comments) {
        this.comments = comments;
    }
}