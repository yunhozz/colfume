package colfume.domain.evaluation.model.repository;

import colfume.domain.evaluation.dto.query.EvaluationQueryDto;

import java.util.List;

public interface EvaluationRepositoryCustom {

    List<EvaluationQueryDto> findListByPerfumeId(Long perfumeId);
}