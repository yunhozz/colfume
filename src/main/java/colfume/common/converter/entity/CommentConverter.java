package colfume.common.converter.entity;

import colfume.api.dto.evaluation.CommentRequestDto;
import colfume.domain.evaluation.model.entity.Comment;
import colfume.domain.evaluation.model.entity.Evaluation;
import colfume.domain.evaluation.service.dto.CommentResponseDto;
import colfume.domain.member.model.entity.Member;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentConverter<W extends Member, E extends Evaluation> implements EntityConverter<Comment, CommentRequestDto, CommentResponseDto> {

    private final W writer;
    private final E evaluation;

    @Override
    public Comment convertToEntity(CommentRequestDto commentRequestDto) {
        return Comment.createParent(writer, evaluation, commentRequestDto.getContent());
    }

    public Comment convertToChildEntity(CommentRequestDto commentRequestDto, Comment parent) {
        return Comment.createChild(parent, writer, evaluation, commentRequestDto.getContent());
    }

    @Override
    public CommentResponseDto convertToDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getWriter().getId(),
                comment.getEvaluation().getId(),
                comment.getContent(),
                comment.getCreatedDate(),
                comment.getLastModifiedDate()
        );
    }
}