package colfume.domain.perfume.model.repository;

import colfume.dto.SearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static colfume.dto.PerfumeDto.*;

public interface PerfumeRepositoryCustom {

    Page<PerfumeSimpleResponseDto> findSimplePerfumeList(SearchDto searchDto, Pageable pageable);
}