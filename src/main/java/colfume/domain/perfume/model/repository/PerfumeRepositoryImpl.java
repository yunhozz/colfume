package colfume.domain.perfume.model.repository;

import colfume.common.enums.ColorType;
import colfume.common.enums.SortCondition;
import colfume.domain.perfume.dto.query.ColorQueryDto;
import colfume.domain.perfume.dto.query.HashtagQueryDto;
import colfume.domain.perfume.dto.query.MoodQueryDto;
import colfume.domain.perfume.dto.query.NoteQueryDto;
import colfume.domain.perfume.dto.query.PerfumeQueryDto;
import colfume.domain.perfume.dto.query.PerfumeSearchQueryDto;
import colfume.domain.perfume.dto.query.PerfumeSimpleQueryDto;
import colfume.domain.perfume.dto.query.QColorQueryDto;
import colfume.domain.perfume.dto.query.QHashtagQueryDto;
import colfume.domain.perfume.dto.query.QMoodQueryDto;
import colfume.domain.perfume.dto.query.QNoteQueryDto;
import colfume.domain.perfume.dto.query.QPerfumeQueryDto;
import colfume.domain.perfume.dto.query.QPerfumeSearchQueryDto;
import colfume.domain.perfume.dto.query.QPerfumeSimpleQueryDto;
import colfume.domain.perfume.dto.query.QStyleQueryDto;
import colfume.domain.perfume.dto.query.StyleQueryDto;
import colfume.domain.perfume.dto.request.SortRequestDto;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static colfume.domain.bookmark.model.entity.QBookmark.bookmark;
import static colfume.domain.member.model.entity.QMember.member;
import static colfume.domain.perfume.model.entity.QColor.color;
import static colfume.domain.perfume.model.entity.QHashtag.hashtag;
import static colfume.domain.perfume.model.entity.QMood.mood;
import static colfume.domain.perfume.model.entity.QNote.note;
import static colfume.domain.perfume.model.entity.QPerfume.perfume;
import static colfume.domain.perfume.model.entity.QStyle.style;

@Repository
@RequiredArgsConstructor
public class PerfumeRepositoryImpl implements PerfumeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public PerfumeQueryDto findPerfumeById(Long id, Long userId) {
        PerfumeQueryDto perfumeDto = queryFactory
                .select(new QPerfumeQueryDto(
                        perfume.id,
                        perfume.name,
                        perfume.volume,
                        perfume.price,
                        perfume.description,
                        perfume.imageUrl,
                        perfume.numOfLikes,
                        perfume.evaluationCount,
                        perfume.score,
                        isBookmarkExist(userId),
                        perfume.createdDate,
                        perfume.lastModifiedDate
                ))
                .from(perfume)
                .leftJoin(bookmark).on(perfume.eq(bookmark.perfume))
                .leftJoin(member).on(bookmark.member.eq(member))
                .where(perfume.id.eq(id))
                .fetchOne();

        Long perfumeId = perfumeDto.getId();
        joinPerfumeWithTables(perfumeDto, perfumeId);

        return perfumeDto;
    }

    @Override
    public Page<PerfumeSimpleQueryDto> findSimplePerfumePage(Long perfumeId, Long userId, Pageable pageable) {
        List<PerfumeSimpleQueryDto> perfumes = queryFactory
                .select(new QPerfumeSimpleQueryDto(
                        perfume.id,
                        perfume.name,
                        perfume.volume,
                        perfume.price,
                        perfume.imageUrl,
                        isBookmarkExist(userId)
                ))
                .from(perfume)
                .leftJoin(bookmark).on(perfume.eq(bookmark.perfume))
                .leftJoin(member).on(bookmark.member.eq(member))
                .where(perfumeIdLt(perfumeId))
                .orderBy(perfume.createdDate.desc())
                .limit(pageable.getPageSize())
                .fetch();

        List<Long> perfumeIds = extractPerfumeIds(perfumes);
        joinPerfumesWithHashtagAndColor(perfumes, perfumeIds);

        return new PageImpl<>(perfumes, pageable, totalCount());
    }

    @Override
    public Page<PerfumeSimpleQueryDto> sortSimplePerfumePage(SortRequestDto sortRequestDto, Long perfumeId, Long userId, Pageable pageable) {
        List<PerfumeSimpleQueryDto> perfumes = queryFactory
                .select(new QPerfumeSimpleQueryDto(
                        perfume.id,
                        perfume.name,
                        perfume.volume,
                        perfume.price,
                        perfume.imageUrl,
                        isBookmarkExist(userId)
                ))
                .distinct() // SortDto 의 colorTypes 에 여러 데이터가 존재할 경우에 의한 N+1 문제 제거
                .from(perfume)
                .leftJoin(bookmark).on(perfume.eq(bookmark.perfume))
                .leftJoin(member).on(bookmark.member.eq(member))
                .join(perfume.colors, color)
                .where(perfumeIdLt(perfumeId))
                .where(
                        volumeGoe(sortRequestDto.getVolumeGoe()),
                        volumeLoe(sortRequestDto.getVolumeLoe()),
                        priceGoe(sortRequestDto.getPriceGoe()),
                        priceLoe(sortRequestDto.getPriceLoe()),
                        byColors(sortRequestDto.getColorTypes())
                )
                .orderBy(bySorting(sortRequestDto.getCondition()))
                .limit(pageable.getPageSize())
                .fetch();

        List<Long> perfumeIds = extractPerfumeIds(perfumes);
        joinPerfumesWithHashtagAndColor(perfumes, perfumeIds);

        return new PageImpl<>(perfumes, pageable, totalCount());
    }

    @Override
    public Page<PerfumeSimpleQueryDto> searchByKeywordOrderByCreated(String keyword, Long perfumeId, Long userId, Pageable pageable) {
        List<PerfumeSimpleQueryDto> perfumes = queryFactory
                .select(new QPerfumeSimpleQueryDto(
                        perfume.id,
                        perfume.name,
                        perfume.volume,
                        perfume.price,
                        perfume.imageUrl,
                        isBookmarkExist(userId)
                ))
                .distinct() // OneToMany, ElementCollection 에 의한 N+1 문제 제거
                .from(perfume)
                .leftJoin(bookmark).on(perfume.eq(bookmark.perfume))
                .leftJoin(member).on(bookmark.member.eq(member))
                .join(perfume.moods, mood)
                .join(perfume.styles, style)
                .join(perfume.notes, note)
                .join(perfume.hashtags, hashtag)
                .where(perfumeIdLt(perfumeId))
                .where(byKeyword(keyword))
                .orderBy(perfume.createdDate.desc())
                .limit(pageable.getPageSize())
                .fetch();

        List<Long> perfumeIds = extractPerfumeIds(perfumes);
        joinPerfumesWithHashtagAndColor(perfumes, perfumeIds);

        return new PageImpl<>(perfumes, pageable, totalCount());
    }

    @Override
    public Page<PerfumeSimpleQueryDto> searchByKeywordOrderByAccuracy(String keyword, Long perfumeId, Long userId, Pageable pageable) {
        List<PerfumeSearchQueryDto> searchPerfumes = queryFactory
                .select(new QPerfumeSearchQueryDto(
                        perfume.id,
                        perfume.name,
                        perfume.description,
                        mood.moodValue,
                        style.styleValue,
                        note.noteValue,
                        hashtag.tag
                ))
                .distinct() // OneToMany, ElementCollection 에 의한 N+1 문제 제거
                .from(perfume)
                .join(perfume.moods, mood)
                .join(perfume.styles, style)
                .join(perfume.notes, note)
                .join(perfume.hashtags, hashtag)
                .where(byKeyword(keyword))
                .fetch();

        List<Long> perfumeIds = extractPerfumeIdsOrderByKeywordDesc(keyword, searchPerfumes);

        List<PerfumeSimpleQueryDto> perfumes = queryFactory
                .select(new QPerfumeSimpleQueryDto(
                        perfume.id,
                        perfume.name,
                        perfume.volume,
                        perfume.price,
                        perfume.imageUrl,
                        isBookmarkExist(userId)
                ))
                .from(perfume)
                .leftJoin(bookmark).on(perfume.eq(bookmark.perfume))
                .leftJoin(member).on(bookmark.member.eq(member))
                .where(perfumeIdLt(perfumeId))
                .where(perfume.id.in(perfumeIds))
                .orderBy(byFieldList(perfumeIds)) // IN 절 순서 보장
                .fetch();

        joinPerfumesWithHashtagAndColor(perfumes, perfumeIds);

        return new PageImpl<>(perfumes, pageable, totalCount());
    }

    private List<Long> extractPerfumeIds(List<PerfumeSimpleQueryDto> perfumes) {
        return perfumes.stream()
                .map(PerfumeSimpleQueryDto::getId).toList();
    }

    private List<Long> extractPerfumeIdsOrderByKeywordDesc(String keyword, List<PerfumeSearchQueryDto> searchPerfumes) {
        Map<PerfumeSearchQueryDto, Integer> numberOfKeywordMap = mappingByCountOfKeyword(keyword, searchPerfumes);
        List<Map.Entry<PerfumeSearchQueryDto, Integer>> sortedEntryList = numberOfKeywordMap.entrySet().stream()
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue())).toList();

        return sortedEntryList.stream()
                .map(entry -> entry.getKey().getId()).toList();
    }

    private Map<PerfumeSearchQueryDto, Integer> mappingByCountOfKeyword(String keyword, List<PerfumeSearchQueryDto> searchPerfumes) {
        return new HashMap<>() {{
            searchPerfumes.forEach(searchPerfume -> {
                int count = 0;
                count += countKeyword(searchPerfume.getName(), keyword);
                count += countKeyword(searchPerfume.getDescription(), keyword);
                count += countKeyword(searchPerfume.getTag(), keyword);
                count += countKeyword(searchPerfume.getMood(), keyword);
                count += countKeyword(searchPerfume.getStyle(), keyword);
                count += countKeyword(searchPerfume.getNote(), keyword);
                put(searchPerfume, count);
            });
        }};
    }

    private int countKeyword(String str, String keyword) {
        return str.length() - str.replace(keyword, "").length();
    }

    private void joinPerfumeWithTables(PerfumeQueryDto perfumeDto, Long perfumeId) {
        List<MoodQueryDto> moods = queryFactory
                .select(new QMoodQueryDto(
                        perfume.id,
                        mood.moodValue
                ))
                .from(perfume)
                .join(perfume.moods, mood)
                .where(perfume.id.eq(perfumeId))
                .fetch();

        List<StyleQueryDto> styles = queryFactory
                .select(new QStyleQueryDto(
                        perfume.id,
                        style.styleValue
                ))
                .from(perfume)
                .join(perfume.styles, style)
                .where(perfume.id.eq(perfumeId))
                .fetch();

        List<NoteQueryDto> notes = queryFactory
                .select(new QNoteQueryDto(
                        perfume.id,
                        note.stage,
                        note.noteValue
                ))
                .from(perfume)
                .join(perfume.notes, note)
                .where(perfume.id.eq(perfumeId))
                .fetch();

        List<HashtagQueryDto> hashtags = queryFactory
                .select(new QHashtagQueryDto(
                        hashtag.id,
                        perfume.id,
                        hashtag.tag
                ))
                .from(hashtag)
                .join(hashtag.perfume, perfume)
                .where(perfume.id.eq(perfumeId))
                .fetch();

        List<ColorQueryDto> colors = queryFactory
                .select(new QColorQueryDto(
                        color.id,
                        perfume.id,
                        color.colorType
                ))
                .from(color)
                .join(color.perfume, perfume)
                .where(perfume.id.eq(perfumeId))
                .fetch();

        perfumeDto.setFields(moods, styles, notes, hashtags, colors);
    }

    private void joinPerfumesWithHashtagAndColor(List<PerfumeSimpleQueryDto> perfumes, List<Long> perfumeIds) {
        List<HashtagQueryDto> hashtags = queryFactory
                .select(new QHashtagQueryDto(
                        hashtag.id,
                        perfume.id,
                        hashtag.tag
                ))
                .from(hashtag)
                .join(hashtag.perfume, perfume)
                .where(perfume.id.in(perfumeIds))
                .fetch();

        List<ColorQueryDto> colors = queryFactory
                .select(new QColorQueryDto(
                        color.id,
                        perfume.id,
                        color.colorType
                ))
                .from(color)
                .join(color.perfume, perfume)
                .where(perfume.id.in(perfumeIds))
                .fetch();

        Map<Long, List<HashtagQueryDto>> hashtagMap = hashtags.stream().collect(Collectors.groupingBy(HashtagQueryDto::getPerfumeId));
        perfumes.forEach(perfumeSimpleQueryDto -> perfumeSimpleQueryDto.setHashtags(hashtagMap.get(perfumeSimpleQueryDto.getId())));

        Map<Long, List<ColorQueryDto>> colorMap = colors.stream().collect(Collectors.groupingBy(ColorQueryDto::getPerfumeId));
        perfumes.forEach(perfumeSimpleQueryDto -> perfumeSimpleQueryDto.setColors(colorMap.get(perfumeSimpleQueryDto.getId())));
    }

    private Long totalCount() {
        return queryFactory
                .select(perfume.count())
                .from(perfume)
                .fetchOne();
    }

    private BooleanExpression isBookmarkExist(Long userId) {
        return new CaseBuilder()
                .when(bookmark.isNotNull().and(member.id.eq(userId)).and(bookmark.isDeleted.isFalse()))
                .then(true)
                .otherwise(false);
    }

    private BooleanExpression perfumeIdLt(Long perfumeId) {
        return perfumeId != null ? perfume.id.lt(perfumeId) : null;
    }

    private BooleanExpression byKeyword(String keyword) {
        if (StringUtils.hasText(keyword)) {
            return perfume.name.contains(keyword)
                    .or(hashtag.tag.contains(keyword))
                    .or(mood.moodValue.contains(keyword))
                    .or(style.styleValue.contains(keyword))
                    .or(note.noteValue.contains(keyword));
        }
        return null;
    }

    private BooleanExpression volumeGoe(Integer volumeGoe) {
        return volumeGoe != null ? perfume.volume.goe(volumeGoe) : null;
    }

    private BooleanExpression volumeLoe(Integer volumeLoe) {
        return volumeLoe != null ? perfume.volume.loe(volumeLoe) : null;
    }

    private BooleanExpression priceGoe(Integer priceGoe) {
        return priceGoe != null ? perfume.price.goe(priceGoe) : null;
    }

    private BooleanExpression priceLoe(Integer priceLoe) {
        return priceLoe != null ? perfume.price.loe(priceLoe) : null;
    }

    private BooleanExpression byColors(List<ColorType> colorTypes) {
        return !colorTypes.isEmpty() ? color.colorType.in(colorTypes) : null;
    }

    private OrderSpecifier<?> bySorting(SortCondition condition) {
        if (condition == SortCondition.LATEST) {
            return perfume.createdDate.desc();
        }
        if (condition == SortCondition.POPULARITY) {
            return perfume.numOfLikes.desc();
        }
        return null;
    }

    private OrderSpecifier<?> byFieldList(List<Long> perfumeIds) {
        return Expressions.stringTemplate("FIELD({0}, {1})", perfume.id, perfumeIds).asc();
    }
}