package colfume.domain.perfume.model.repository;

import colfume.domain.perfume.model.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
}
