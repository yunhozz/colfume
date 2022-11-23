package colfume.domain.evaluation.service.converter;

import colfume.common.EntityConverter;
import colfume.domain.evaluation.dto.request.CommentRequestDto;
import colfume.domain.evaluation.dto.response.CommentResponseDto;
import colfume.domain.evaluation.model.entity.Comment;
import colfume.domain.evaluation.model.entity.Evaluation;
import colfume.domain.member.model.entity.Member;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CommentConverter implements EntityConverter<Comment, CommentRequestDto, CommentResponseDto> {

    private Member writer;
    private Evaluation evaluation;
    private Comment parent;

    @Override
    public Comment convertToEntity(CommentRequestDto commentRequestDto) {
        return Comment.createParent(writer, evaluation, commentRequestDto.getContent());
    }

    public Comment convertToChildEntity(CommentRequestDto commentRequestDto) {
        return Comment.createChild(parent, writer, parent.getEvaluation(), commentRequestDto.getContent());
    }

    @Override
    public CommentResponseDto convertToDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getWriter().getId(),
                comment.getEvaluation().getId(),
                comment.getContent(),
                comment.getCreatedDate(),
                comment.getLastModifiedDate(),
                comment.getChildren().stream()
                        .map(CommentResponseDto.CommentChildResponseDto::new)
                        .collect(Collectors.toList())
        );
    }

    public void setEntitiesForParent(Member writer, Evaluation evaluation) {
        this.writer = writer;
        this.evaluation = evaluation;
    }

    public void setEntitiesForChild(Member writer, Comment parent) {
        this.writer = writer;
        this.parent = parent;
    }
}