package colfume.domain.bookmark.model.repository;

import colfume.domain.bookmark.model.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
}