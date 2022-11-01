package colfume.domain.evaluation.model.repository;

import colfume.domain.evaluation.model.repository.dto.EvaluationQueryDto;

import java.util.List;

public interface EvaluationRepositoryCustom {

    List<EvaluationQueryDto> findEvaluationListByPerfumeId(Long perfumeId);
}