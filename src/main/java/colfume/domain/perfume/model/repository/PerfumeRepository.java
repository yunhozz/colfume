package colfume.domain.perfume.model.repository;

import colfume.domain.perfume.model.entity.Perfume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PerfumeRepository extends JpaRepository<Perfume, Long>, PerfumeRepositoryCustom {

    @Modifying(clearAutomatically = true)
    @Query("update Perfume p set p.score = ((p.score * (p.evaluationCount - 1)) + :score) / p.evaluationCount where p.id = :id")
    void updateScoreForAdd(@Param("id") Long perfumeId, @Param("score") int score); // 평가 추가시 반영

    @Modifying(clearAutomatically = true)
    @Query("update Perfume p set p.score = ((p.score * (p.evaluationCount + 1)) - :score) / p.evaluationCount where p.id = :id")
    void updateScoreForSubtract(@Param("id") Long perfumeId, @Param("score") int score); // 평가 삭제시 반영
}