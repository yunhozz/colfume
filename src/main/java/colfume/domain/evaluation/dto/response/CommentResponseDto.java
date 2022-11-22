package colfume.domain.evaluation.dto.response;

import colfume.domain.evaluation.model.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    private List<CommentChildResponseDto> children;

    @Getter
    @NoArgsConstructor
    public static class CommentChildResponseDto {

        private Long id;
        private Long writerId;
        private Long parentId;
        private String content;
        private LocalDateTime createdDate;
        private LocalDateTime lastModifiedDate;

        public CommentChildResponseDto(Comment child) {
            id = child.getId();
            writerId = child.getWriter().getId();
            parentId = child.getParent().getId();
            content = child.getContent();
            createdDate = child.getCreatedDate();
            lastModifiedDate = child.getLastModifiedDate();
        }
    }
}