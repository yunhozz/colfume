package colfume.domain.evaluation.model.repository;

import colfume.domain.evaluation.model.repository.dto.EvaluationQueryDto;
import colfume.domain.evaluation.model.repository.dto.QEvaluationQueryDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static colfume.domain.evaluation.model.entity.QEvaluation.evaluation;
import static colfume.domain.member.model.entity.QMember.member;
import static colfume.domain.perfume.model.entity.QPerfume.perfume;

@Repository
@RequiredArgsConstructor
public class EvaluationRepositoryImpl implements EvaluationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<EvaluationQueryDto> findEvaluationListByPerfumeId(Long perfumeId) {
        return queryFactory
                .select(new QEvaluationQueryDto(
                        evaluation.id,
                        evaluation.content,
                        evaluation.score,
                        evaluation.createdDate,
                        evaluation.lastModifiedDate,
                        member.id,
                        member.name,
                        member.imageUrl
                ))
                .from(evaluation)
                .join(evaluation.writer, member)
                .join(evaluation.perfume, perfume)
                .where(perfume.id.eq(perfumeId), evaluation.isDeleted.isFalse())
                .orderBy(evaluation.createdDate.asc())
                .fetch();
    }
}