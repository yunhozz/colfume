package colfume.domain.perfume.model.repository;

import colfume.domain.perfume.model.entity.Perfume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PerfumeRepository extends JpaRepository<Perfume, Long>, PerfumeRepositoryCustom {

    // 벌크성 쿼리는 가급적 다른 엔티티와 연관된 것 없이 단일 엔티티에 대해서만 사용하자

    @Modifying(clearAutomatically = true)
    @Query("update Perfume p set p.score = ((p.score * (p.evaluationCount - 1)) + :score) / p.evaluationCount where p.id = :id")
    void updateScoreForAdd(@Param("id") Long id, @Param("score") double score); // 평가 추가시 반영

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Perfume p set p.score = (p.score * p.evaluationCount - :os + :ns) / p.evaluationCount where p.id = :id")
    void updateScoreForModify(@Param("id") Long id, @Param("os") double oldScore, @Param("ns") double newScore); // 평가 업데이트시 반영

    @Modifying(clearAutomatically = true)
    @Query("update Perfume p set p.score = ((p.score * (p.evaluationCount + 1)) - :score) / p.evaluationCount where p.id = :id")
    void updateScoreForSubtract(@Param("id") Long id, @Param("score") double score); // 평가 삭제시 반영
}