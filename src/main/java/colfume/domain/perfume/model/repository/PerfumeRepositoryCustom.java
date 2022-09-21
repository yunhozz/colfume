package colfume.domain.perfume.model.repository;

import colfume.dto.SortDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static colfume.dto.PerfumeDto.*;

public interface PerfumeRepositoryCustom {

    Page<PerfumeSimpleResponseDto> searchByKeywordOrderByCreated(String keyword, Pageable pageable);
    Page<PerfumeSimpleResponseDto> searchByKeywordOrderByAccuracy(String keyword, Pageable pageable);
    Page<PerfumeSimpleResponseDto> sortSimplePerfumeList(SortDto sortDto, Pageable pageable);
}