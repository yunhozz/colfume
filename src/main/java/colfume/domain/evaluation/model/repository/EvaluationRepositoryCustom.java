package colfume.domain.evaluation.model.repository;

import java.util.List;

import static colfume.common.dto.EvaluationDto.*;

public interface EvaluationRepositoryCustom {

    List<EvaluationQueryDto> findEvaluationListByPerfumeId(Long perfumeId);
}