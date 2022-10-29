package colfume.domain.perfume.model.repository;

import colfume.domain.perfume.model.repository.dto.ColorResponseDto;
import colfume.domain.perfume.model.repository.dto.HashtagResponseDto;
import colfume.domain.perfume.model.repository.dto.PerfumeSearchQueryDto;
import colfume.domain.perfume.model.repository.dto.PerfumeSimpleQueryDto;
import colfume.domain.perfume.model.repository.dto.QPerfumeSimpleQueryDto;
import colfume.dto.SortDto;
import colfume.enums.ColorType;
import colfume.enums.SortCondition;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static colfume.domain.perfume.model.entity.QColor.color;
import static colfume.domain.perfume.model.entity.QHashtag.hashtag;
import static colfume.domain.perfume.model.entity.QPerfume.perfume;

// TODO : 커서 페이징 방식으로 구현
@Repository
@RequiredArgsConstructor
public class PerfumeRepositoryImpl implements PerfumeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PerfumeSimpleQueryDto> searchByKeywordOrderByCreated(String keyword, Pageable pageable) {
        List<PerfumeSimpleQueryDto> perfumes = queryFactory
                .select(new QPerfumeSimpleQueryDto(
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

        List<Long> perfumeIds = extractPerfumeIds(perfumes);
        joinQueryWithHashtagAndColor(perfumes, perfumeIds);

        return new PageImpl<>(perfumes, pageable, perfumes.size());
    }

    // TODO : @ElementCollection 인 컬럼을 querydsl 로 어떻게 처리할지?
    @Override
    public Page<PerfumeSimpleQueryDto> searchByKeywordOrderByAccuracy(String keyword, Pageable pageable) {
        List<PerfumeSearchQueryDto> searchPerfumes = queryFactory
                .select(Projections.constructor(
                        PerfumeSearchQueryDto.class,
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

        List<Long> perfumeIds = extractPerfumeIdsOrderByKeywordDesc(keyword, searchPerfumes);

        List<PerfumeSimpleQueryDto> perfumes = queryFactory
                .select(new QPerfumeSimpleQueryDto(
                        perfume.id,
                        perfume.name,
                        perfume.volume,
                        perfume.price,
                        perfume.imageUrl
                ))
                .from(perfume)
                .join(perfume.hashtags, hashtag)
                .where(perfume.id.in(perfumeIds))
                .orderBy(orderByFieldList(perfumeIds)) // IN 절 순서 보장
                .fetch();

        joinQueryWithHashtagAndColor(perfumes, perfumeIds);

        return new PageImpl<>(perfumes, pageable, perfumes.size());
    }

    @Override
    public Page<PerfumeSimpleQueryDto> sortSimplePerfumeList(SortDto sortDto, Pageable pageable) {
        List<PerfumeSimpleQueryDto> perfumes = queryFactory
                .select(new QPerfumeSimpleQueryDto(
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

        List<Long> perfumeIds = extractPerfumeIds(perfumes);
        joinQueryWithHashtagAndColor(perfumes, perfumeIds);

        return new PageImpl<>(perfumes, pageable, perfumes.size());
    }

    private List<Long> extractPerfumeIds(List<PerfumeSimpleQueryDto> perfumes) {
        return perfumes.stream().map(PerfumeSimpleQueryDto::getId).toList();
    }

    private List<Long> extractPerfumeIdsOrderByKeywordDesc(String keyword, List<PerfumeSearchQueryDto> searchPerfumes) {
        Map<PerfumeSearchQueryDto, Integer> numberOfKeywordMap = new HashMap<>();
        searchPerfumes.forEach(searchPerfume -> {
            int count = 0;
            count += countKeyword(searchPerfume.getName(), keyword);
            count += countKeyword(searchPerfume.getDescription(), keyword);

            numberOfKeywordMap.put(searchPerfume, count);
        });

        List<Map.Entry<PerfumeSearchQueryDto, Integer>> sortedEntryList = numberOfKeywordMap.entrySet().stream()
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue())).toList();

        return sortedEntryList.stream().map(entry -> entry.getKey().getId()).toList();
    }

    private int countKeyword(String str, String keyword) {
        return str.length() - str.replace(keyword, "").length();
    }

    private void joinQueryWithHashtagAndColor(List<PerfumeSimpleQueryDto> perfumes, List<Long> perfumeIds) {
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
        perfumes.forEach(perfumeSimpleQueryDto -> perfumeSimpleQueryDto.setHashtags(hashtagMap.get(perfumeSimpleQueryDto.getId())));

        Map<Long, List<ColorResponseDto>> colorMap = colors.stream().collect(Collectors.groupingBy(ColorResponseDto::getPerfumeId));
        perfumes.forEach(perfumeSimpleQueryDto -> perfumeSimpleQueryDto.setColors(colorMap.get(perfumeSimpleQueryDto.getId())));
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

    private OrderSpecifier<?> orderByFieldList(List<Long> perfumeIds) {
        return Expressions.stringTemplate("FIELD({0}, {1})", perfume.id, perfumeIds).asc();
    }
}