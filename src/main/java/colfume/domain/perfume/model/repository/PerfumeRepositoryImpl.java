package colfume.domain.perfume.model.repository;

import colfume.api.dto.perfume.SortDto;
import colfume.common.enums.ColorType;
import colfume.common.enums.SortCondition;
import colfume.domain.perfume.model.repository.dto.ColorQueryDto;
import colfume.domain.perfume.model.repository.dto.HashtagQueryDto;
import colfume.domain.perfume.model.repository.dto.PerfumeSearchQueryDto;
import colfume.domain.perfume.model.repository.dto.PerfumeSimpleQueryDto;
import colfume.domain.perfume.model.repository.dto.QColorQueryDto;
import colfume.domain.perfume.model.repository.dto.QHashtagQueryDto;
import colfume.domain.perfume.model.repository.dto.QPerfumeSimpleQueryDto;
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
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static colfume.domain.perfume.model.entity.QColor.color;
import static colfume.domain.perfume.model.entity.QHashtag.hashtag;
import static colfume.domain.perfume.model.entity.QPerfume.perfume;

@Repository
@RequiredArgsConstructor
public class PerfumeRepositoryImpl implements PerfumeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PerfumeSimpleQueryDto> findSimplePerfumePage(Long perfumeId, Pageable pageable) {
        List<PerfumeSimpleQueryDto> perfumes = queryFactory
                .select(new QPerfumeSimpleQueryDto(
                        perfume.id,
                        perfume.name,
                        perfume.volume,
                        perfume.price,
                        perfume.imageUrl
                ))
                .from(perfume)
                .where(perfumeIdLt(perfumeId))
                .orderBy(perfume.createdDate.desc())
                .limit(pageable.getPageSize())
                .fetch();

        List<Long> perfumeIds = extractPerfumeIds(perfumes);
        joinQueryWithHashtagAndColor(perfumes, perfumeIds);

        return new PageImpl<>(perfumes, pageable, perfumes.size());
    }

    @Override
    public Page<PerfumeSimpleQueryDto> sortSimplePerfumePage(SortDto sortDto, Long perfumeId, Pageable pageable) {
        List<PerfumeSimpleQueryDto> perfumes = queryFactory
                .select(new QPerfumeSimpleQueryDto(
                        perfume.id,
                        perfume.name,
                        perfume.volume,
                        perfume.price,
                        perfume.imageUrl
                ))
                .distinct() // perfume <> colors join 시 N+1 문제 방지
                .from(perfume)
                .join(perfume.colors, color)
                .where(perfumeIdLt(perfumeId))
                .where(
                        volumeGoe(sortDto.getVolumeGoe()),
                        volumeLoe(sortDto.getVolumeLoe()),
                        priceGoe(sortDto.getPriceGoe()),
                        priceLoe(sortDto.getPriceLoe()),
                        byColors(sortDto.getColorTypes())
                )
                .orderBy(bySorting(sortDto.getCondition()))
                .limit(pageable.getPageSize())
                .fetch();

        List<Long> perfumeIds = extractPerfumeIds(perfumes);
        joinQueryWithHashtagAndColor(perfumes, perfumeIds);

        return new PageImpl<>(perfumes, pageable, perfumes.size());
    }

    @Override
    public Page<PerfumeSimpleQueryDto> searchByKeywordOrderByCreated(String keyword, Long perfumeId, Pageable pageable) {
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
                .where(perfumeIdLt(perfumeId))
                .where(byKeyword(keyword))
                .orderBy(perfume.createdDate.desc())
                .limit(pageable.getPageSize())
                .fetch();

        List<Long> perfumeIds = extractPerfumeIds(perfumes);
        joinQueryWithHashtagAndColor(perfumes, perfumeIds);

        return new PageImpl<>(perfumes, pageable, perfumes.size());
    }

    // TODO : @ElementCollection 인 컬럼을 querydsl 로 어떻게 처리할지?
    @Override
    public Page<PerfumeSimpleQueryDto> searchByKeywordOrderByAccuracy(String keyword, Long perfumeId, Pageable pageable) {
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
                .where(byKeyword(keyword))
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
                .where(perfumeIdLt(perfumeId))
                .where(perfume.id.in(perfumeIds))
                .orderBy(byFieldList(perfumeIds)) // IN 절 순서 보장
                .fetch();

        joinQueryWithHashtagAndColor(perfumes, perfumeIds);

        return new PageImpl<>(perfumes, pageable, perfumes.size());
    }

    private List<Long> extractPerfumeIds(List<PerfumeSimpleQueryDto> perfumes) {
        return perfumes.stream().map(PerfumeSimpleQueryDto::getId).toList();
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
                put(searchPerfume, count);
            });
        }};
    }

    private int countKeyword(String str, String keyword) {
        return str.length() - str.replace(keyword, "").length();
    }

    private void joinQueryWithHashtagAndColor(List<PerfumeSimpleQueryDto> perfumes, List<Long> perfumeIds) {
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

    private BooleanExpression perfumeIdLt(Long perfumeId) {
        return perfumeId != null ? perfume.id.lt(perfumeId) : null;
    }

    private BooleanExpression byKeyword(String keyword) {
        return StringUtils.hasText(keyword) ? perfume.name.contains(keyword).or(hashtag.tag.contains(keyword)) : null;
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