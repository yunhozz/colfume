package colfume.domain.bookmark.model.repository;

import colfume.domain.bookmark.model.repository.dto.BookmarkQueryDto;
import colfume.domain.bookmark.model.repository.dto.QBookmarkQueryDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static colfume.domain.bookmark.model.entity.QBookmark.bookmark;
import static colfume.domain.member.model.entity.QMember.member;
import static colfume.domain.perfume.model.entity.QPerfume.perfume;

@Repository
@RequiredArgsConstructor
public class BookmarkRepositoryImpl implements BookmarkRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BookmarkQueryDto> findBookmarkListByUserId(Long userId) {
        return queryFactory
                .select(new QBookmarkQueryDto(
                        bookmark.id,
                        perfume.id,
                        perfume.name,
                        perfume.imageUrl
                ))
                .from(bookmark)
                .join(bookmark.member, member)
                .join(bookmark.perfume, perfume)
                .where(member.id.eq(userId), bookmark.isDeleted.isFalse())
                .orderBy(bookmark.lastModifiedDate.desc())
                .fetch();
    }
}