package colfume.domain.evaluation.model.repository;

import colfume.domain.evaluation.model.entity.Evaluation;
import colfume.domain.perfume.model.entity.Perfume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    List<Evaluation> findByPerfume(Perfume perfume);
}