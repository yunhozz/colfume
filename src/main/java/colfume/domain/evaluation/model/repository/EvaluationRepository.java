package colfume.domain.evaluation.model.repository;

import colfume.domain.evaluation.model.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long>, EvaluationRepositoryCustom {

    @Query("select e from Evaluation e join e.writer w join fetch e.perfume p where e.id = :id and w.id = :userId")
    Optional<Evaluation> findWithPerfumeByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
}