package colfume.domain.perfume.model.repository;

import colfume.api.dto.perfume.SortDto;
import colfume.domain.perfume.model.repository.dto.PerfumeSimpleQueryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PerfumeRepositoryCustom {

    Page<PerfumeSimpleQueryDto> findSimplePerfumePage(Long perfumeId, Pageable pageable);
    Page<PerfumeSimpleQueryDto> sortSimplePerfumePage(SortDto sortDto, Long perfumeId, Pageable pageable);
    Page<PerfumeSimpleQueryDto> searchByKeywordOrderByCreated(String keyword, Long perfumeId, Pageable pageable);
    Page<PerfumeSimpleQueryDto> searchByKeywordOrderByAccuracy(String keyword, Long perfumeId, Pageable pageable);
}