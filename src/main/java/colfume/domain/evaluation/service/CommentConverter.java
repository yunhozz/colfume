package colfume.domain.evaluation.service;

import colfume.common.EntityConverter;
import colfume.domain.evaluation.dto.request.CommentRequestDto;
import colfume.domain.evaluation.dto.response.CommentResponseDto;
import colfume.domain.evaluation.model.entity.Comment;
import colfume.domain.evaluation.model.entity.Evaluation;
import colfume.domain.member.model.entity.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentConverter implements EntityConverter<Comment, CommentRequestDto, CommentResponseDto> {

    private Member writer;
    private Evaluation evaluation;
    private Comment parent;

    protected CommentConverter(Member writer, Evaluation evaluation) {
        this.writer = writer;
        this.evaluation = evaluation;
    }

    protected CommentConverter(Member writer, Comment parent) {
        this.writer = writer;
        this.parent = parent;
    }

    @Override
    public Comment convertToEntity(CommentRequestDto commentRequestDto) {
        if (writer == null || evaluation == null) {
            throw new IllegalStateException("연관된 엔티티가 생성되지 않았습니다.");
        }

        return Comment.createParent(writer, evaluation, commentRequestDto.getContent());
    }

    public Comment convertToChildEntity(CommentRequestDto commentRequestDto) {
        if (writer == null || parent == null) {
            throw new IllegalStateException("연관된 엔티티가 생성되지 않았습니다.");
        }

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
}