package colfume.domain.perfume.model.repository;

import colfume.domain.perfume.model.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<Color, Long> {
}