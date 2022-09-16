package colfume.domain.perfume.model.repository;

import colfume.dto.QPerfumeDto_PerfumeSimpleResponseDto;
import colfume.dto.SearchDto;
import colfume.enums.ColorType;
import colfume.enums.SearchCondition;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
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
    public Page<PerfumeSimpleResponseDto> findSimplePerfumeList(SearchDto searchDto, Pageable pageable) {
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
                        volumeGoe(searchDto.getVolumeGoe()),
                        volumeLoe(searchDto.getVolumeLoe()),
                        priceGoe(searchDto.getPriceGoe()),
                        priceLoe(searchDto.getPriceLoe()),
                        byColors(searchDto.getColorTypes())
                )
                .orderBy(byCondition(searchDto.getCondition()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Long> perfumeIds = perfumes.stream().map(PerfumeSimpleResponseDto::getId).toList();

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

        Long count = queryFactory
                .select(perfume.count())
                .from(perfume)
                .fetchOne();

        return new PageImpl<>(perfumes, pageable, count);
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

    private OrderSpecifier<?> byCondition(SearchCondition condition) {
        if (condition == SearchCondition.LATEST) {
            return perfume.createdDate.desc();
        }
        if (condition == SearchCondition.POPULARITY) {
            return perfume.numOfLikes.desc();
        }
        return null;
    }
}