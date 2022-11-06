package colfume.domain.bookmark.model.repository;

import colfume.domain.bookmark.model.entity.Bookmark;
import colfume.domain.member.model.entity.Member;
import colfume.domain.perfume.model.entity.Perfume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, BookmarkRepositoryCustom {

    @Query("select p.id from Bookmark b join b.perfume p where b.id = :id")
    Optional<Long> findPerfumeIdByBookmarkId(@Param("id") Long bookmarkId);

    Optional<Bookmark> findByMemberAndPerfume(Member member, Perfume perfume);
}