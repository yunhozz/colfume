package colfume.domain.perfume.model.repository;

import colfume.domain.perfume.model.repository.dto.PerfumeSimpleQueryDto;
import colfume.api.dto.perfume.SortDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PerfumeRepositoryCustom {

    Page<PerfumeSimpleQueryDto> searchByKeywordOrderByCreated(String keyword, Pageable pageable);
    Page<PerfumeSimpleQueryDto> searchByKeywordOrderByAccuracy(String keyword, Pageable pageable);
    Page<PerfumeSimpleQueryDto> sortSimplePerfumeList(SortDto sortDto, Pageable pageable);
}