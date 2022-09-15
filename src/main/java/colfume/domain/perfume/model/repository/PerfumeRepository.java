package colfume.domain.perfume.model.repository;

import colfume.domain.perfume.model.entity.Perfume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfumeRepository extends JpaRepository<Perfume, Long>, PerfumeRepositoryCustom {
}