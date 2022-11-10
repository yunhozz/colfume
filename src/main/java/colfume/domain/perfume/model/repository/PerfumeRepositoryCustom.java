package colfume.domain.perfume.model.repository;

import colfume.api.dto.perfume.SortDto;
import colfume.domain.perfume.model.repository.dto.PerfumeQueryDto;
import colfume.domain.perfume.model.repository.dto.PerfumeSimpleQueryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PerfumeRepositoryCustom {

    PerfumeQueryDto findPerfumeById(Long id, Long userId);
    Page<PerfumeSimpleQueryDto> findSimplePerfumePage(Long perfumeId, Long userId, Pageable pageable);
    Page<PerfumeSimpleQueryDto> sortSimplePerfumePage(SortDto sortDto, Long perfumeId, Long userId, Pageable pageable);
    Page<PerfumeSimpleQueryDto> searchByKeywordOrderByCreated(String keyword, Long perfumeId, Long userId, Pageable pageable);
    Page<PerfumeSimpleQueryDto> searchByKeywordOrderByAccuracy(String keyword, Long perfumeId, Long userId, Pageable pageable);
}