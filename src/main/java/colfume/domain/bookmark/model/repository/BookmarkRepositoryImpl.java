package colfume.domain.bookmark.model.repository;

import colfume.dto.QBookmarkDto_BookmarkQueryDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static colfume.domain.bookmark.model.entity.QBookmark.*;
import static colfume.domain.member.model.entity.QMember.*;
import static colfume.domain.perfume.model.entity.QPerfume.*;
import static colfume.common.dto.BookmarkDto.*;

@Repository
@RequiredArgsConstructor
public class BookmarkRepositoryImpl implements BookmarkRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BookmarkQueryDto> findBookmarkListByUserId(Long userId) {
        return queryFactory
                .select(new QBookmarkDto_BookmarkQueryDto(
                        bookmark.id,
                        bookmark.createdDate,
                        perfume.id,
                        perfume.name,
                        perfume.imageUrl,
                        member.id
                ))
                .from(bookmark)
                .join(bookmark.perfume, perfume)
                .join(bookmark.member, member)
                .where(member.id.eq(userId), bookmark.isDeleted.isFalse())
                .orderBy(bookmark.createdDate.desc())
                .fetch();
    }
}