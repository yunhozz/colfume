package colfume.domain.evaluation.model.repository;

import colfume.dto.QEvaluationDto_EvaluationQueryDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static colfume.domain.evaluation.model.entity.QEvaluation.*;
import static colfume.domain.member.model.entity.QMember.*;
import static colfume.domain.perfume.model.entity.QPerfume.*;
import static colfume.dto.EvaluationDto.*;

@Repository
@RequiredArgsConstructor
public class EvaluationRepositoryImpl implements EvaluationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<EvaluationQueryDto> findEvaluationListByPerfumeId(Long perfumeId) {
        return queryFactory
                .select(new QEvaluationDto_EvaluationQueryDto(
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