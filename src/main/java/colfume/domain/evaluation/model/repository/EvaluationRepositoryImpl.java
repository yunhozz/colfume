package colfume.domain.evaluation.model.repository;

import colfume.domain.evaluation.dto.query.CommentQueryDto;
import colfume.domain.evaluation.dto.query.EvaluationQueryDto;
import colfume.domain.evaluation.dto.query.QCommentQueryDto;
import colfume.domain.evaluation.dto.query.QEvaluationQueryDto;
import colfume.domain.member.model.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static colfume.domain.evaluation.model.entity.QComment.comment;
import static colfume.domain.evaluation.model.entity.QEvaluation.evaluation;
import static colfume.domain.perfume.model.entity.QPerfume.perfume;

@Repository
@RequiredArgsConstructor
public class EvaluationRepositoryImpl implements EvaluationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // 향수 상세페이지 -> 평가 -> 평가 목록과 각각의 댓글 목록, 대댓글은 조회 x
    @Override
    public List<EvaluationQueryDto> findListByPerfumeId(Long perfumeId) {
        QMember writer = new QMember("writer");

        List<EvaluationQueryDto> evaluationList = queryFactory
                .select(new QEvaluationQueryDto(
                        evaluation.id,
                        evaluation.content,
                        evaluation.score,
                        evaluation.createdDate,
                        evaluation.lastModifiedDate,
                        writer.id,
                        writer.name,
                        writer.imageUrl
                ))
                .from(evaluation)
                .join(evaluation.writer, writer)
                .join(evaluation.perfume, perfume)
                .where(perfume.id.eq(perfumeId))
                .orderBy(evaluation.createdDate.asc())
                .fetch();

        List<Long> evaluationIds = extractEvaluationIds(evaluationList);

        List<CommentQueryDto> commentList = queryFactory
                .select(new QCommentQueryDto(
                        comment.id,
                        comment.content,
                        comment.createdDate,
                        comment.lastModifiedDate,
                        evaluation.id,
                        writer.id,
                        writer.name,
                        writer.imageUrl
                ))
                .from(comment)
                .join(comment.evaluation, evaluation)
                .join(comment.writer, writer)
                .where(
                        evaluation.id.in(evaluationIds),
                        comment.parent.isNull() // 대댓글 조회 x
                )
                .orderBy(comment.createdDate.asc())
                .fetch();

        combineIntoOne(evaluationList, commentList);

        return evaluationList;
    }

    private List<Long> extractEvaluationIds(List<EvaluationQueryDto> evaluationList) {
        return evaluationList.stream()
                .map(EvaluationQueryDto::getId).toList();
    }

    private void combineIntoOne(List<EvaluationQueryDto> evaluationList, List<CommentQueryDto> commentList) {
        Map<Long, List<CommentQueryDto>> commentListMap = commentList.stream()
                .collect(Collectors.groupingBy(CommentQueryDto::getEvaluationId));
        evaluationList.forEach(evaluationQueryDto -> evaluationQueryDto.setComments(commentListMap.get(evaluationQueryDto.getId())));
    }
}