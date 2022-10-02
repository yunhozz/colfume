package colfume.domain.evaluation.model.repository;

import colfume.domain.evaluation.model.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long>, EvaluationRepositoryCustom {
}