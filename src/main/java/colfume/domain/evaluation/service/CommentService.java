package colfume.domain.evaluation.service;

import colfume.common.enums.ErrorCode;
import colfume.domain.evaluation.dto.request.CommentRequestDto;
import colfume.domain.evaluation.dto.response.CommentResponseDto;
import colfume.domain.evaluation.model.entity.Comment;
import colfume.domain.evaluation.model.entity.Evaluation;
import colfume.domain.evaluation.model.repository.CommentRepository;
import colfume.domain.evaluation.model.repository.EvaluationRepository;
import colfume.domain.evaluation.service.converter.CommentConverter;
import colfume.domain.evaluation.service.exception.CommentNotFoundException;
import colfume.domain.evaluation.service.exception.CrudNotAuthenticationException;
import colfume.domain.evaluation.service.exception.EvaluationAlreadyDeletedException;
import colfume.domain.evaluation.service.exception.EvaluationNotFoundException;
import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final EvaluationRepository evaluationRepository;
    private final MemberRepository memberRepository;
    private final CommentConverter converter;

    @Transactional
    public Long createComment(CommentRequestDto commentRequestDto, Long writerId, Long evaluationId) {
        Member writer = memberRepository.getReferenceById(writerId);
        Evaluation evaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new EvaluationNotFoundException(ErrorCode.EVALUATION_NOT_FOUND));

        if (evaluation.isDeleted()) {
            throw new EvaluationAlreadyDeletedException(ErrorCode.ALREADY_DELETED);
        }

        converter.setEntitiesForParent(writer, evaluation);
        Comment comment = converter.convertToEntity(commentRequestDto);

        return commentRepository.save(comment).getId();
    }

    @Transactional
    public Long createChildComment(CommentRequestDto commentRequestDto, Long parentId, Long writerId) {
        Member writer = memberRepository.getReferenceById(writerId);
        Comment parent = commentRepository.findWithEvaluationById(parentId)
                .orElseThrow(() -> new CommentNotFoundException(ErrorCode.COMMENT_NOT_FOUND));

        converter.setEntitiesForChild(writer, parent);
        Comment child = converter.convertToChildEntity(commentRequestDto);

        return commentRepository.save(child).getId();
    }

    @Transactional
    public void updateContent(Long commentId, Long userId, CommentRequestDto commentRequestDto) {
        Comment comment = validateAuthorization(commentId, userId);
        comment.updateContent(commentRequestDto.getContent());
    }

    // TODO: 2022-11-19 삭제 로직 고민
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = validateAuthorization(commentId, userId);
        commentRepository.delete(comment); // cascade delete: children
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> findDtoListByEvaluationId(Long evaluationId) {
        return commentRepository.findListByEvaluationId(evaluationId).stream()
                .map(converter::convertToDto)
                .collect(Collectors.toList());
    }

    private Comment validateAuthorization(Long commentId, Long userId) {
        Optional<Comment> optionalComment = commentRepository.findByIdAndUserId(commentId, userId);

        if (optionalComment.isEmpty()) {
            throw new CrudNotAuthenticationException(ErrorCode.NOT_AUTHENTICATED);
        }
        return optionalComment.get();
    }
}