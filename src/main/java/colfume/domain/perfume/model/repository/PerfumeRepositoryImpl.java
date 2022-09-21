package colfume.domain.perfume.model.repository;

import colfume.dto.QPerfumeDto_PerfumeSimpleResponseDto;
import colfume.dto.SortDto;
import colfume.enums.ColorType;
import colfume.enums.SortCondition;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static colfume.domain.perfume.model.entity.QColor.*;
import static colfume.domain.perfume.model.entity.QHashtag.*;
import static colfume.domain.perfume.model.entity.QPerfume.*;
import static colfume.dto.PerfumeDto.*;

@Repository
@RequiredArgsConstructor
public class PerfumeRepositoryImpl implements PerfumeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PerfumeSimpleResponseDto> searchByKeywordOrderByCreated(String keyword, Pageable pageable) {
        List<PerfumeSimpleResponseDto> perfumes = queryFactory
                .select(new QPerfumeDto_PerfumeSimpleResponseDto(
                        perfume.id,
                        perfume.name,
                        perfume.volume,
                        perfume.price,
                        perfume.imageUrl
                ))
                .from(perfume)
                .join(perfume.hashtags, hashtag)
                .where(
                        perfume.name.contains(keyword)
                                .or(perfume.description.contains(keyword))
                                .or(perfume.moods.any().contains(keyword))
                                .or(perfume.styles.any().contains(keyword))
                                .or(perfume.notes.any().contains(keyword))
                                .or(hashtag.tag.contains(keyword))
                )
                .orderBy(perfume.createdDate.desc())
                .fetch();

        List<Long> perfumeIds = perfumes.stream().map(PerfumeSimpleResponseDto::getId).toList();
        joinQueryWithHashtagAndColor(perfumes, perfumeIds);

        Long count = queryFactory
                .select(perfume.count())
                .from(perfume)
                .fetchOne();

        return new PageImpl<>(perfumes, pageable, count);
    }

    // TODO : @ElementCollection 인 컬럼을 querydsl 로 어떻게 처리할지?
    @Override
    public Page<PerfumeSimpleResponseDto> searchByKeywordOrderByAccuracy(String keyword, Pageable pageable) {
        List<PerfumeSearchResponseDto> searchPerfumes = queryFactory
                .select(Projections.constructor(
                        PerfumeSearchResponseDto.class,
                        perfume.id,
                        perfume.name,
                        perfume.description,
                        hashtag.tag
                ))
                .from(perfume)
                .join(perfume.hashtags, hashtag)
                .where(
                        perfume.name.contains(keyword)
                                .or(perfume.description.contains(keyword))
                                .or(perfume.moods.any().contains(keyword))
                                .or(perfume.styles.any().contains(keyword))
                                .or(perfume.notes.any().contains(keyword))
                                .or(hashtag.tag.contains(keyword))
                )
                .fetch();

        Map<PerfumeSearchResponseDto, Integer> numberOfKeywordMap = new HashMap<>();
        searchPerfumes.forEach(searchPerfume -> {
            int count = 0;
            count += countKeyword(searchPerfume.getName(), keyword);
            count += countKeyword(searchPerfume.getDescription(), keyword);

            numberOfKeywordMap.put(searchPerfume, count);
        });

        List<Map.Entry<PerfumeSearchResponseDto, Integer>> sortedEntryList = numberOfKeywordMap.entrySet().stream()
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue())).toList();
        List<Long> perfumeIds = sortedEntryList.stream().map(entry -> entry.getKey().getId()).toList();

        List<PerfumeSimpleResponseDto> perfumes = queryFactory
                .select(new QPerfumeDto_PerfumeSimpleResponseDto(
                        perfume.id,
                        perfume.name,
                        perfume.volume,
                        perfume.price,
                        perfume.imageUrl
                ))
                .from(perfume)
                .join(perfume.hashtags, hashtag)
                .where(perfume.id.in(perfumeIds))
                .fetch();

        joinQueryWithHashtagAndColor(perfumes, perfumeIds);

        Long count = queryFactory
                .select(perfume.count())
                .from(perfume)
                .fetchOne();

        return new PageImpl<>(perfumes, pageable, count);
    }

    @Override
    public Page<PerfumeSimpleResponseDto> sortSimplePerfumeList(SortDto sortDto, Pageable pageable) {
        List<PerfumeSimpleResponseDto> perfumes = queryFactory
                .select(new QPerfumeDto_PerfumeSimpleResponseDto(
                        perfume.id,
                        perfume.name,
                        perfume.volume,
                        perfume.price,
                        perfume.imageUrl
                ))
                .from(perfume)
                .join(perfume.colors, color)
                .where(
                        volumeGoe(sortDto.getVolumeGoe()),
                        volumeLoe(sortDto.getVolumeLoe()),
                        priceGoe(sortDto.getPriceGoe()),
                        priceLoe(sortDto.getPriceLoe()),
                        byColors(sortDto.getColorTypes())
                )
                .orderBy(bySorting(sortDto.getCondition()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Long> perfumeIds = perfumes.stream().map(PerfumeSimpleResponseDto::getId).toList();
        joinQueryWithHashtagAndColor(perfumes, perfumeIds);

        Long count = queryFactory
                .select(perfume.count())
                .from(perfume)
                .fetchOne();

        return new PageImpl<>(perfumes, pageable, count);
    }

    private void joinQueryWithHashtagAndColor(List<PerfumeSimpleResponseDto> perfumes, List<Long> perfumeIds) {
        List<HashtagResponseDto> hashtags = queryFactory
                .select(Projections.constructor(
                        HashtagResponseDto.class,
                        hashtag.id,
                        perfume.id,
                        hashtag.tag
                ))
                .from(hashtag)
                .join(hashtag.perfume, perfume)
                .where(perfume.id.in(perfumeIds))
                .fetch();

        List<ColorResponseDto> colors = queryFactory
                .select(Projections.constructor(
                        ColorResponseDto.class,
                        color.id,
                        perfume.id,
                        color.colorType
                ))
                .from(color)
                .join(color.perfume, perfume)
                .where(perfume.id.in(perfumeIds))
                .fetch();

        Map<Long, List<HashtagResponseDto>> hashtagMap = hashtags.stream().collect(Collectors.groupingBy(HashtagResponseDto::getPerfumeId));
        perfumes.forEach(perfumeSimpleResponseDto -> perfumeSimpleResponseDto.setHashtags(hashtagMap.get(perfumeSimpleResponseDto.getId())));

        Map<Long, List<ColorResponseDto>> colorMap = colors.stream().collect(Collectors.groupingBy(ColorResponseDto::getPerfumeId));
        perfumes.forEach(perfumeSimpleResponseDto -> perfumeSimpleResponseDto.setColors(colorMap.get(perfumeSimpleResponseDto.getId())));
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
        return colorTypes != null ? color.colorType.in(colorTypes) : null;
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

    private int countKeyword(String str, String keyword) {
        return str.length() - str.replace(keyword, "").length();
    }
}