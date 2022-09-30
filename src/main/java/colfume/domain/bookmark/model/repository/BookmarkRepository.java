package colfume.domain.bookmark.model.repository;

import colfume.domain.bookmark.model.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, BookmarkRepositoryCustom {

    @Query("select p.id from Bookmark b join b.perfume p where b.id = :id")
    Optional<Long> findPerfumeIdByBookmarkId(@Param("id") Long bookmarkId);
}