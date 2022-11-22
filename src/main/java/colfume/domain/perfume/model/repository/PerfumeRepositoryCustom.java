package colfume.domain.perfume.model.repository;

import colfume.domain.perfume.dto.request.SortRequestDto;
import colfume.domain.perfume.dto.query.PerfumeQueryDto;
import colfume.domain.perfume.dto.query.PerfumeSimpleQueryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PerfumeRepositoryCustom {

    PerfumeQueryDto findPerfumeById(Long id, Long userId);
    Page<PerfumeSimpleQueryDto> findSimplePerfumePage(Long perfumeId, Long userId, Pageable pageable);
    Page<PerfumeSimpleQueryDto> sortSimplePerfumePage(SortRequestDto sortRequestDto, Long perfumeId, Long userId, Pageable pageable);
    Page<PerfumeSimpleQueryDto> searchByKeywordOrderByCreated(String keyword, Long perfumeId, Long userId, Pageable pageable);
    Page<PerfumeSimpleQueryDto> searchByKeywordOrderByAccuracy(String keyword, Long perfumeId, Long userId, Pageable pageable);
}